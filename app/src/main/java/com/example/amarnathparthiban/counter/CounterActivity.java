package com.example.amarnathparthiban.counter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CounterActivity extends AppCompatActivity {

    private static final int increment_values[] = {1, 2, 5, 10, 25, 100};
    private static ArrayList<Integer> list_counts = new ArrayList<>();
    private static ArrayList<String> list_names = new ArrayList<>();
    private static int curr_pos = 0;
    private static int increment = 0;
    private static char increment_sign = '+';
    private static ListAdapter adapter;
    private EditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.counter);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        //prevents keyboard from adjusting layout
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        //setup on initial startup
        if (savedInstanceState == null) {

            list_names.add(getString(R.string.InitialName));
            list_counts.add(0);

        }

        name = (EditText) findViewById(R.id.Name);

        //setup Clicker button and incrementer button
        //((Button) findViewById(R.id.Clicker)).setText(String.valueOf(list_counts.get(curr_pos)));
        ((Button) findViewById(R.id.Incrementer))
                .setText(increment_sign + String.valueOf(increment_values[increment]));

        //alternate way to keep data stored
        /*
        if (savedInstanceState != null) {

            name.setText(savedInstanceState.getString("CURR_NAME"));
            curr_pos  = savedInstanceState.getInt("CURR_COUNT");
            increment   = savedInstanceState.getInt("INCREMENT");
            list_counts = savedInstanceState.getIntegerArrayList("COUNTS");
            list_names  = savedInstanceState.getStringArrayList("NAMES");

        }
        */

        //initialize layout
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.RelativeLayout);

        //setup ListView rows as miniture layouts
        adapter = new ListAdapter(this, R.layout.list_row, list_names, list_counts);
        final ListView list = (ListView) findViewById(R.id.List);
        list.setAdapter(adapter);
        list.setLongClickable(true);
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                //sets position of values to adjust
                curr_pos = position;

                //sets name to correct position and update
                name.setText(list_names.get(curr_pos));
                ((Button) findViewById(R.id.Clicker))
                        .setText(String.valueOf(list_counts.get(curr_pos)));
                adapter.notifyDataSetChanged();

                return true;
            }
        });

        //setup of clicker button
        Button clicker = (Button) (findViewById(R.id.Clicker));
        clicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                int multplier = -1;

                //adjust the sign to increment by
                if (increment_sign == '+')
                    multplier = 1;

                //increments value
                list_counts.set(curr_pos, list_counts.get(curr_pos)
                        + multplier * increment_values[increment]);

                //set the value on listRow and update
                ((Button) findViewById(R.id.Clicker))
                        .setText(String.valueOf(list_counts.get(curr_pos)));
                adapter.notifyDataSetChanged();

            }

        });

        //set up incrementer button
        final Button incrementer = (Button) (findViewById(R.id.Incrementer));
        incrementer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //cycles incrementer in loop
                if (increment == 5)
                    increment = 0;
                else
                    increment++;

                //updates incrementer button
                ((Button) findViewById(R.id.Incrementer)).setText(increment_sign +
                        String.valueOf(increment_values[increment]));

            }

        });

        //change increments from positive to negative, vice-versa
        incrementer.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {

                //changes sign to opposite
                if (increment_sign == '+')
                    increment_sign = '-';
                else
                    increment_sign = '+';

                //update button for new sign
                ((Button) findViewById(R.id.Incrementer)).setText(increment_sign +
                        String.valueOf(increment_values[increment]));

                return true;

            }
        });

        //setup clear button
        Button clear = (Button) (findViewById(R.id.Clear));
        clear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                list_counts.set(curr_pos, 0);
                ((Button) findViewById(R.id.Clicker))
                        .setText(String.valueOf(list_counts.get(curr_pos)));

                adapter.notifyDataSetChanged();

            }
        });

        //setup full clear
        clear.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {

                full_clear();

                return true;

            }
        });


        Button new_count = (Button) (findViewById(R.id.New));
        new_count.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String new_name;
                int entry_number = 0;

                do {

                    entry_number++;
                    new_name = "Count " + (entry_number);

                } while (list_names.contains(new_name));

                list_names.add(new_name);
                list_counts.add(0);
                curr_pos += 1;
                adapter.notifyDataSetChanged();

                name.setText(new_name);
                ((Button) findViewById(R.id.Clicker))
                        .setText(String.valueOf(list_counts.get(curr_pos)));

            }

        });

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                list_names.set(curr_pos, name.getText().toString());
                adapter.notifyDataSetChanged();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void full_clear() {

        list_names.clear();
        list_counts.clear();
        list_names.add(getString(R.string.InitialName));
        list_counts.add(0);
        curr_pos = 0;

        name.setText(list_names.get(curr_pos));
        ((Button) findViewById(R.id.Clicker))
                .setText(String.valueOf(list_counts.get(curr_pos)));

        adapter.notifyDataSetChanged();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    /*
    private void list_remove(int position) {

        list_counts.remove(position);
        list_names.remove(position);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("CURR_NAME", name.getText().toString());
        outState.putInt("CURR_COUNT", curr_pos);
        outState.putInt("INCREMENT", increment);
        outState.putIntegerArrayList("COUNTS", list_counts);
        outState.putStringArrayList("NAMES", list_names);

    }
    */

    private class ListAdapter extends ArrayAdapter<String> {

        private int layout;

        public ListAdapter(Context context, int resource, List<String> list_names,
                           List<Integer> list_counts) {
            super(context, resource, list_names);

            layout = resource;

        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder mainViewHolder = null;

            if (convertView == null) {

                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);

                ViewHolder viewHolder = new ViewHolder();
                viewHolder.name = (TextView) convertView.findViewById(R.id.List_Name);
                viewHolder.count = (TextView) convertView.findViewById(R.id.List_Count);
                viewHolder.delete = (Button) convertView.findViewById(R.id.Delete);

                convertView.setTag(viewHolder);

            }

            mainViewHolder = (ViewHolder) convertView.getTag();
            mainViewHolder.delete.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {


                    list_counts.remove(position);
                    list_names.remove(position);

                    if (curr_pos == list_counts.size())
                        curr_pos -= 1;

                    adapter.notifyDataSetChanged();

                    if (curr_pos < 0) {

                        full_clear();

                    }

                    name.setText(list_names.get(curr_pos));
                    ((Button) findViewById(R.id.Clicker))
                            .setText(String.valueOf(list_counts.get(curr_pos)));


                }

            });

            mainViewHolder.name.setText(list_names.get(position));
            mainViewHolder.count.setText(list_counts.get(position).toString());

            return convertView;

        }

    }

    private class ViewHolder {

        TextView name;
        TextView count;
        Button delete;

    }

}
