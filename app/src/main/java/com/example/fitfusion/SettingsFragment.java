package com.example.fitfusion;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // Initialize all the cards
        CardView cardProfile = view.findViewById(R.id.cardProfile);
        CardView cardNotification = view.findViewById(R.id.cardNotification);
        CardView cardPreferences = view.findViewById(R.id.cardPreferences);
        CardView cardPrivacy = view.findViewById(R.id.cardPrivacy);
        CardView cardFitnessTracking = view.findViewById(R.id.cardFitnessTracking);
        CardView cardSubscriptions = view.findViewById(R.id.cardSubscriptions);
        CardView cardCustomization = view.findViewById(R.id.cardCustomization);
        CardView cardCommunity = view.findViewById(R.id.cardCommunity);
        CardView cardHelp = view.findViewById(R.id.cardHelp);
        CardView cardLogout = view.findViewById(R.id.cardLogout);

        // Set click listeners for each card
        cardProfile.setOnClickListener(v -> navigateTo(Profile.class));
        cardNotification.setOnClickListener(v -> navigateTo(Notification.class));
        cardPreferences.setOnClickListener(v -> navigateTo(Preferences.class));
        cardPrivacy.setOnClickListener(v -> navigateTo(Privacy.class));
        cardFitnessTracking.setOnClickListener(v -> navigateTo(FitnessTracker.class));
        cardSubscriptions.setOnClickListener(v -> navigateTo(Subscription.class));
        cardCustomization.setOnClickListener(v -> navigateTo(AppCustomization.class));
        cardCommunity.setOnClickListener(v -> navigateTo(Community.class));
        cardHelp.setOnClickListener(v -> navigateTo(Help.class));
        cardLogout.setOnClickListener(v -> navigateTo(Logout.class));

        return view;
    }

    // Helper method to navigate to the respective activity
    private void navigateTo(Class<?> targetActivity) {
        Intent intent = new Intent(getActivity(), targetActivity);
        startActivity(intent);
    }
}
