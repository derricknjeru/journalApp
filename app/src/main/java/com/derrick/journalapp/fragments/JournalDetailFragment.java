package com.derrick.journalapp.fragments;


import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.derrick.journalapp.R;
import com.derrick.journalapp.contracts.JournalDetailContract;
import com.derrick.journalapp.databinding.FragmentJournalDetailBinding;
import com.derrick.journalapp.ui.AddEditJournalActivity;

import static com.derrick.journalapp.fragments.AddEditJournalFragment.ARGUMENT_EDIT_JOURNAL_ID;
import static com.derrick.journalapp.fragments.AddEditJournalFragment.ARGUMENT_USER_ID;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link JournalDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JournalDetailFragment extends Fragment implements JournalDetailContract.View {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private JournalDetailContract.Presenter mPresenter;

    private FragmentJournalDetailBinding mBinding;


    @NonNull
    private static final int REQUEST_EDIT_JOURNAL = 1;
    TextView mTxtTitle;
    TextView mTxtDesc;

    public JournalDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment JournalDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static JournalDetailFragment newInstance(String param1, String param2) {
        JournalDetailFragment fragment = new JournalDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_journal_detail, container, false);
        View v = mBinding.getRoot();

        // Set up floating action button
        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.fab_edit);

        // Set up floating action button
        mTxtTitle = getActivity().findViewById(R.id.merchant_title);
        // Set up floating action button

        mTxtDesc = getActivity().findViewById(R.id.txt_description);

        fab.setImageResource(R.drawable.ic_edit_white_24dp);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.editJournal();
            }
        });


        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                mPresenter.deleteJournal();
                return true;
        }
        return false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.journal_details_menu, menu);
    }


    @Override
    public void setLoadingIndicator(boolean active) {
        if (active) {
            mBinding.journalDetailTitle.setText("");
            mBinding.journalDetailDescription.setText(getString(R.string.loading));
        }
    }

    @Override
    public void showMissingJournal() {
        mBinding.journalDetailTitle.setText("");
        mBinding.journalDetailDescription.setText(getString(R.string.no_data));
    }

    @Override
    public void hideTitle() {
        mBinding.journalDetailTitle.setVisibility(View.GONE);
        mTxtTitle.setVisibility(View.GONE);
    }

    @Override
    public void showTitle(String title) {

        mBinding.journalDetailTitle.setVisibility(View.GONE);
        mBinding.journalDetailTitle.setText(title);

        mTxtTitle.setVisibility(View.VISIBLE);
        mTxtTitle.setText(title);
    }

    @Override
    public void hideDescription() {
        mBinding.journalDetailDescription.setVisibility(View.GONE);
        mTxtDesc.setVisibility(View.VISIBLE);
    }

    @Override
    public void showDescription(String description) {
        mBinding.journalDetailDescription.setVisibility(View.VISIBLE);
        mBinding.journalDetailDescription.setText(description);

        mTxtDesc.setVisibility(View.GONE);
        mTxtDesc.setText(description);
    }

    @Override
    public void showEditJournal(long journalId, String userId) {
        Intent intent = new Intent(getContext(), AddEditJournalActivity.class);
        intent.putExtra(ARGUMENT_USER_ID, userId);
        intent.putExtra(ARGUMENT_EDIT_JOURNAL_ID, journalId);
        startActivityForResult(intent, AddEditJournalActivity.REQUEST_ADD_JOURNAL);

    }


    @Override
    public void showJournalDeleted() {
        getActivity().finish();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void hideTime() {
        mBinding.time.setVisibility(View.GONE);
    }

    @Override
    public void showTime(String time) {
        mBinding.time.setVisibility(View.VISIBLE);
        mBinding.time.setText(time);
    }

    @Override
    public void hideDate() {
        mBinding.date.setVisibility(View.GONE);
    }

    @Override
    public void showDate(String date) {
        mBinding.date.setVisibility(View.VISIBLE);
        mBinding.date.setText(date);
    }

    @Override
    public void setPresenter(JournalDetailContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_EDIT_JOURNAL) {
            // If the task was edited successfully, go back to the list.
            if (resultCode == Activity.RESULT_OK) {
                getActivity().finish();
            }
        }
    }

}
