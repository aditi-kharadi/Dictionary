package in.jobs.wordict;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

class PagerViewAdapter extends FragmentPagerAdapter {

    public PagerViewAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;
        switch(position)
        {
            case 0:
                fragment = new Meaning();
                break;
            case 1:
                fragment = new Defenition();
                break;
            case 2:
                fragment = new Synonyms();
                break;
            case 3:
                fragment = new Antonyms();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 4;
    }
}
