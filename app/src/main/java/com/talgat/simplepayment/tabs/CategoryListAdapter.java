package com.talgat.simplepayment.tabs;

import android.content.Context;
import android.widget.ArrayAdapter;
import com.talgat.simplepayment.database.Category;

import java.util.Collection;
import java.util.List;

/**
 * Created by Talgat on 16.06.2015.
 */
public class CategoryListAdapter extends ArrayAdapter<Category> {

    List<Category> categories;
    private final Context context;

    public CategoryListAdapter(Context context, int resource,
                               List<Category> objects) {
        super(context, resource, objects);
        this.categories = objects;
        this.context = context;
    }

    public void add(Category cat, int position) {
        categories.add(position, cat);
    }

    @Override
    public void addAll(Collection<? extends Category> collection) {
        for (Category category : collection) {
            add(category);
        }
    }
}
