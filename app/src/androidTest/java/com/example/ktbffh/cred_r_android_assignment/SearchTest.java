package com.example.ktbffh.cred_r_android_assignment;

import android.support.test.filters.MediumTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by ktbffh on 09/12/17.
 */
@MediumTest
@RunWith(AndroidJUnit4.class)
public class SearchTest {
    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void searchResults() throws Exception {
        MainActivity activity = rule.getActivity();
        ActionMenuItemView actionMenuItemView = (ActionMenuItemView) activity.findViewById(R.id.search);
        MenuItem menuitem = actionMenuItemView.getItemData();
        android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) MenuItemCompat.getActionView(menuitem);
        assertThat(actionMenuItemView, notNullValue());
        actionMenuItemView.requestFocus();
        searchView.setQuery(activity.getResources().getString(R.string.test_search_string), true);
        View viewById = activity.findViewById(R.id.recycler_view);
        assertThat(viewById, notNullValue());
        RecyclerView listView = (RecyclerView) viewById;
        ItemAdapter adapter = (ItemAdapter) listView.getAdapter();
        assertThat(adapter, instanceOf(ItemAdapter.class));
    }

    @After
    public void tearDown() throws Exception {
        synchronized (this) {
            wait();
        }
    }
}
