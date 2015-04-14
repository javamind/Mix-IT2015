package com.ehret.mixit.utils;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Editable;
import android.text.Html;
import android.text.Layout;
import android.text.Spannable;
import android.text.style.BulletSpan;
import android.text.style.LeadingMarginSpan;
import android.text.style.TypefaceSpan;

import org.xml.sax.XMLReader;

import java.util.Vector;

/**
 * Created by ehret_g on 14/04/15.
 */
public class HtmlTagHandler implements Html.TagHandler {
    private int mListItemCount = 0;
    private Vector<String> mListParents = new Vector<>();

    @Override
    public void handleTag(final boolean opening, final String tag, Editable output, final XMLReader xmlReader) {

        if (tag.equals("ul") || tag.equals("ol")) {
            if (opening) {
                mListParents.add("ul");
            }
            else {
                mListParents.remove(tag);
            }

            mListItemCount = 0;
        } else if (tag.equals("li") && !opening) {
            handleListTag(output);
        }

    }

    private void handleListTag(Editable output) {
        if (mListParents.lastElement().equals("ul")) {
            output.append("\n");
            String[] split = output.toString().split("\n");

            int lastIndex = split.length - 1;
            int start = output.length() - split[lastIndex].length() - 1;
            output.setSpan(new BulletSpan(15 * mListParents.size()), start, output.length(), 0);
        }
    }
}