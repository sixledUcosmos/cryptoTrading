package com.example.criptolist.Ui.Activitis;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.example.criptolist.Ui.Fragments.Home;
import com.example.criptolist.Ui.Fragments.ShowCryptos;
import com.example.criptolist.R;
import com.example.criptolist.Ui.Fragments.ListWallet;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Home home = new Home();
        final ShowCryptos list = new ShowCryptos();
        final ListWallet listWallet = new ListWallet();
        loadFragment(home);
        TabLayout menu=findViewById(R.id.menulist);
        menu.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                        switch(tab.getPosition()){
                            case 0:
                                loadFragment(home);
                                break;
                            case 1:
                                loadFragment(list);
                                        break;
                            case 3:
                                loadFragment(listWallet);
                                        break;


                        }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }




    private void loadFragment(Fragment fragment) {
        FragmentManager fm = getFragmentManager();
// create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
// replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.fragments,fragment);
        fragmentTransaction.commit(); // save the changes
    }
}