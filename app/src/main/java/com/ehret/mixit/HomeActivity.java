package com.ehret.mixit;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.ehret.mixit.domain.SendSocial;
import com.ehret.mixit.domain.TypeFile;
import com.ehret.mixit.fragment.DataListFragment;
import com.ehret.mixit.fragment.DialogAboutFragment;
import com.ehret.mixit.fragment.HomeFragment;
import com.ehret.mixit.fragment.PeopleDetailFragment;
import com.ehret.mixit.fragment.PlanningFragment;
import com.ehret.mixit.fragment.SessionDetailFragment;
import com.ehret.mixit.utils.UIUtils;


public class HomeActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    public static final String ARG_SECTION_NUMBER = "section_number";
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer    .
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
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
            editor.commit();
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
        Fragment fragment;
        if (position > 2) {
            fragment = DataListFragment.newInstance(getTypeFile(position).toString(), null, position + 1);
        } else if (position == 1) {
            fragment = new PlanningFragment();
        } else {
            fragment = new HomeFragment();
        }
        changeCurrentFragment(fragment, null);
    }

    public void changeCurrentFragment(Fragment fragment, String backable) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (backable!=null) {
            fragmentTransaction.addToBackStack(backable);
        }
        else{
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        fragmentTransaction.replace(R.id.container, fragment).commit();
    }

    public void onSectionAttached(String title, String color) {
        mTitle = getString(getResources().getIdentifier(title, "string", HomeActivity.this.getPackageName()));
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(
                        getResources().getColor(
                                getResources().getIdentifier(color, "color", HomeActivity.this.getPackageName()))));

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
                Toast.makeText(this, "TODO", Toast.LENGTH_SHORT).show();
                //chargementDonnees(TypeFile.members);
                return true;
            case R.id.menu_sync_speaker:
                Toast.makeText(this, "TODO", Toast.LENGTH_SHORT).show();
                //chargementDonnees(TypeFile.speaker);
                return true;
            case R.id.menu_sync_talk:
                Toast.makeText(this, "TODO", Toast.LENGTH_SHORT).show();
                //chargementDonnees(TypeFile.talks);
                return true;
            case R.id.menu_sync_favorites:
                Toast.makeText(this, "TODO", Toast.LENGTH_SHORT).show();
                //chargementDonnees(TypeFile.members);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
