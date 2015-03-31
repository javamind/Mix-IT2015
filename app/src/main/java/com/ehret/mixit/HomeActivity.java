package com.ehret.mixit;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.ehret.mixit.domain.JsonFile;
import com.ehret.mixit.domain.SendSocial;
import com.ehret.mixit.domain.TypeFile;
import com.ehret.mixit.domain.people.Membre;
import com.ehret.mixit.fragment.DataListFragment;
import com.ehret.mixit.fragment.DialogAboutFragment;
import com.ehret.mixit.fragment.FilDeLeauFragment;
import com.ehret.mixit.fragment.HomeFragment;
import com.ehret.mixit.fragment.NavigationDrawerFragment;
import com.ehret.mixit.fragment.PeopleDetailFragment;
import com.ehret.mixit.fragment.PlanningFragment;
import com.ehret.mixit.fragment.SessionDetailFragment;
import com.ehret.mixit.model.ConferenceFacade;
import com.ehret.mixit.model.MembreFacade;
import com.ehret.mixit.model.Synchronizer;
import com.ehret.mixit.utils.FileUtils;
import com.ehret.mixit.utils.UIUtils;

import java.util.Arrays;
import java.util.List;


public class HomeActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    public static final String ARG_SECTION_NUMBER = "section_number";
    private Fragment mContent;
    private ProgressDialog progressDialog;
    protected int progressStatus = 0;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SharedPreferences settings = this.getSharedPreferences(UIUtils.ARG_FILE_SAV, 0);
        Boolean test = settings.getBoolean(UIUtils.ARG_KEY_FIRST_TIME, true);
        if(test){
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean(UIUtils.ARG_KEY_FIRST_TIME, false);
            appelerSynchronizer(TypeAppel.TALK, null);
            editor.apply();
        }

        if (savedInstanceState != null) {
            //Restore the fragment's instance
            mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
        }

        NavigationDrawerFragment mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer    .
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Save the fragment's instance
        getSupportFragmentManager().putFragment(outState, "mContent", mContent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Intent.ACTION_SEARCH.equals(getIntent().getAction())) {
            String filterQuery = getIntent().getStringExtra(SearchManager.QUERY);
            int current = PreferenceManager.getDefaultSharedPreferences(this).getInt(ARG_SECTION_NUMBER, 0);
            getSupportFragmentManager().beginTransaction().replace(
                    R.id.container,
                    DataListFragment.newInstance(getTypeFile(current).toString(), filterQuery,
                            current + 1))
                    .commit();
        }
    }

    private TypeFile getTypeFile(int position) {
        if (position > 2) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt(ARG_SECTION_NUMBER, position);
            editor.apply();
        }

        switch (position) {
            case 3:
                return TypeFile.talks;
            case 4:
                return TypeFile.workshops;
            case 5:
                return TypeFile.favorites;
            case 6:
                return TypeFile.lightningtalks;
            case 7:
                return TypeFile.speaker;
            case 8:
                return TypeFile.sponsor;
            case 9:
                return TypeFile.staff;
            default:
                return TypeFile.members;
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        switch (position) {
            case 0:
                mContent = new HomeFragment();
                break;
            case 1:
                mContent = new PlanningFragment();
                break;
            case 2:
                mContent = new FilDeLeauFragment();
                break;
            default:
                mContent = DataListFragment.newInstance(getTypeFile(position).toString(), null, position + 1);
        }
        changeCurrentFragment(mContent, null);
    }

    public void changeCurrentFragment(Fragment fragment, String backable) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (backable != null) {
            fragmentTransaction.addToBackStack(backable);
        } else {
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        fragmentTransaction.replace(R.id.container, fragment).commit();
    }

    public void onSectionAttached(String title, String color) {
        int nbtitle = getResources().getIdentifier(title, "string", HomeActivity.this.getPackageName());

        if (nbtitle > 0) {
            mTitle = getString(nbtitle > 0 ? nbtitle : R.string.title_section_home);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setBackgroundDrawable(
                        new ColorDrawable(
                                getResources().getColor(getResources().getIdentifier(color, "color", HomeActivity.this.getPackageName()))));
            }
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        restoreActionBar();

        //We have to know which fragment is used
        boolean found = false;
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment instanceof DataListFragment) {
                found = true;
                menu.findItem(R.id.menu_search).setVisible(true);
                // Get the SearchView and set the searchable configuration
                SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
                SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
                searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            }
            //The search buuton is not displayed if detail is displayed
            if ((fragment instanceof SessionDetailFragment) || (fragment instanceof PeopleDetailFragment)) {
                found = false;
            }
        }
        if (!found) {
            menu.findItem(R.id.menu_search).setVisible(false);
        }
        menu.findItem(R.id.menu_favorites).setVisible(false);
        menu.findItem(R.id.menu_profile).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = null;
        switch (item.getItemId()) {
            case R.id.menu_about:
                DialogAboutFragment dial = new DialogAboutFragment();
                dial.show(getFragmentManager(), getResources().getString(R.string.about_titre));
                return true;
            case R.id.menu_compose_google:
                UIUtils.sendMessage(this, SendSocial.plus);
                return true;
            case R.id.menu_compose_twitter:
                UIUtils.sendMessage(this, SendSocial.twitter);
                return true;
            case R.id.menu_sync_membre:
                chargementDonnees(TypeAppel.MEMBRE);
                return true;
            case R.id.menu_sync_talk:
                chargementDonnees(TypeAppel.TALK);
                return true;
            case R.id.menu_sync_favorites:
                SharedPreferences settings = getSharedPreferences(UIUtils.PREFS_TEMP_NAME, 0);
                Long id = settings.getLong("idMemberForFavorite", 0L);
                if (id != null && id > 0) {
                    appelerSynchronizer(TypeAppel.FAVORITE, id);
                } else {
                    Toast.makeText(this, getString(R.string.description_link_user_error), Toast.LENGTH_LONG).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private enum TypeAppel {MEMBRE, TALK, FAVORITE}

    /**
     * Affichage d'un message pour savoir quelle données récupérer
     */
    protected void chargementDonnees(final TypeAppel type) {
        if (UIUtils.isNetworkAvailable(getBaseContext())) {
            if (FileUtils.isExternalStorageWritable()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(
                        getString(type == TypeAppel.MEMBRE ? R.string.dial_message_membre : R.string.dial_message_talk))
                        .setPositiveButton(R.string.dial_oui, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                appelerSynchronizer(type, null);
                            }
                        })
                        .setNeutralButton(R.string.dial_cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //On ne fait rien
                            }
                        });
                builder.create();
                builder.show();
            } else {
                Toast.makeText(getBaseContext(), getText(R.string.sync_erreur), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getBaseContext(), getText(R.string.sync_erreur_reseau), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Lancement de la synchro
     */
    protected void appelerSynchronizer(TypeAppel type, Long idUserForFavorite) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
        }
        progressDialog.setCancelable(true);
        int nbMax = 0;
        if (type.equals(TypeAppel.TALK)) {
            nbMax = 100;
        } else if (type.equals(TypeAppel.MEMBRE)) {
            nbMax = 2000;
        } else {
            nbMax = 3;
        }

        progressDialog.setMax(nbMax);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage(getResources().getString(R.string.sync_message));
        progressDialog.show();
        SynchronizeMixitAsync synchronizeMixitAsync = new SynchronizeMixitAsync(idUserForFavorite);
        synchronizeMixitAsync.execute(type);
    }

    /**
     * Lance en asynchrone la recuperation des fichiers
     */
    private class SynchronizeMixitAsync extends AsyncTask<TypeAppel, Integer, Void> {
        private Long idUserForFavorite;

        private SynchronizeMixitAsync(Long idUserForFavorite) {
            this.idUserForFavorite = idUserForFavorite;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressStatus = 0;
        }

        @Override
        protected Void doInBackground(TypeAppel... params) {
            TypeAppel type = params[0];

            if (type.equals(TypeAppel.FAVORITE)) {
                if (Synchronizer.downloadJsonFile(getBaseContext(), String.format(JsonFile.FileFavorites.getUrl(), idUserForFavorite), JsonFile.FileFavorites.getType())) {
                    //Si OK on met a jour les favoris en ecrasant ceux existant
                    ConferenceFacade.getInstance().setFavorites(getBaseContext(), false);
                }
                publishProgress(progressStatus++);
                return null;
            }

            List<JsonFile> jsonToSync = null;
            //En fonction de la demande on télécharge tel ou tel fichier
            switch (type) {
                case TALK:
                    jsonToSync = Arrays.asList(JsonFile.FileLightningTalks, JsonFile.FileInterest, JsonFile.FileTalks, JsonFile.FileSpeaker, JsonFile.FileSponsor);
                    break;
                case MEMBRE:
                    jsonToSync = Arrays.asList(JsonFile.FileMembers, JsonFile.FileSpeaker, JsonFile.FileInterest, JsonFile.FileSponsor, JsonFile.FileStaff);
                    break;
            }

            for (JsonFile json : jsonToSync) {
                try {
                    if (!Synchronizer.downloadJsonFile(getBaseContext(), json.getUrl(), json.getType())) {
                        //Si une erreur de chargement on sort
                        break;
                    }
                    publishProgress(progressStatus++);
                } catch (RuntimeException e) {
                    Log.w("DialogSynchronize", "Impossible de synchroniser", e);
                }
            }

            if (type.equals(TypeAppel.FAVORITE)) {
                return null;
            }

            //Une fois finie on supprime le cache
            List<Membre> membres;
            if (type.equals(TypeAppel.TALK)) {
                MembreFacade.getInstance().viderCacheSpeakerStaffSponsor();
                ConferenceFacade.getInstance().viderCache();
                membres = MembreFacade.getInstance().getMembres(getBaseContext(), TypeFile.speaker.name(), null);
                membres.addAll(MembreFacade.getInstance().getMembres(getBaseContext(), TypeFile.staff.name(), null));
            } else {
                MembreFacade.getInstance().viderCacheMembres();
                membres = MembreFacade.getInstance().getMembres(getBaseContext(), TypeFile.members.name(), null);
            }

            //L'action d'après consiste à charger les images
            for (Membre membre : membres) {
                if (membre.getUrlimage() != null) {
                    Synchronizer.downloadImage(getBaseContext(), membre.getUrlimage(), "membre" + membre.getId());
                    publishProgress(progressStatus++);
                }
            }
            //Pour les sponsors on s'interesse au logo
            for (Membre membre : MembreFacade.getInstance().getMembres(getBaseContext(), TypeFile.sponsor.name(), null)) {
                if (membre.getLogo() != null) {
                    Synchronizer.downloadImage(getBaseContext(), membre.getLogo(), "membre" + membre.getId());
                    publishProgress(progressStatus++);
                }
            }
            return null;
        }

        /**
         * This callback method is invoked when publishProgress()
         * method is called
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressDialog.setProgress(progressStatus);
        }

        /**
         * This callback method is invoked when the background function
         * doInBackground() is executed completely
         */
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            try {
                progressDialog.dismiss();
            } catch (IllegalArgumentException e) {
                //Si la vue n'est plus attachée (changement d'orientation on évite de faire planter)
                Log.w("AbstractActivity", "Erreur à la fin du chargement lors de la notification de la vue");

            }
        }
    }
}
