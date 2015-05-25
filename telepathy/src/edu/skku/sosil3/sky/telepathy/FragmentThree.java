package edu.skku.sosil3.sky.telepathy;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class FragmentThree extends PreferenceFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}
}