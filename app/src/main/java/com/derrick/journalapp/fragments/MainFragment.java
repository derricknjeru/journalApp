package com.derrick.journalapp.fragments;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.derrick.journalapp.R;
import com.derrick.journalapp.adapter.JournalsAdapter;
import com.derrick.journalapp.contracts.JournalsContract;
import com.derrick.journalapp.databinding.FragmentMainBinding;
import com.derrick.journalapp.pojos.Journal;
import com.derrick.journalapp.ui.AddEditJournalActivity;
import com.derrick.journalapp.ui.JournalDetailActivity;
import com.derrick.journalapp.utilities.LogUtils;
import com.derrick.journalapp.utilities.RecyclerClickListener;

import java.util.ArrayList;
import java.util.List;

import static com.derrick.journalapp.fragments.AddEditJournalFragment.ARGUMENT_USER_ID;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment implements JournalsContract.View {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String LOG_TAG = MainFragment.class.getSimpleName();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private JournalsContract.Presenter mPresenter;


    private FragmentMainBinding mBinding;
    private JournalsAdapter mListAdapter;
    private List<Journal> mJournalList = new ArrayList<>();


    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_main, container, false);

        View v = mBinding.getRoot();

        // Set up floating action button
        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.fab_add_journal);

        fab.setImageResource(R.drawable.ic_add_white_24dp);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.addNewJournal();
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
    public void setLoadingIndicator(boolean active) {
        if (active) {
            mBinding.progressBar.setVisibility(View.VISIBLE);
        } else {
            mBinding.progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void showJournals(final List<Journal> journals) {
        mJournalList.clear();
        mJournalList = journals;
        mBinding.list.setVisibility(View.VISIBLE);
        mBinding.tvNoMessage.setVisibility(View.GONE);
        mBinding.imgNoJournal.setVisibility(View.GONE);
        LogUtils.showLog(LOG_TAG, "@all journals::" + journals);


        mListAdapter = new JournalsAdapter(mJournalList, new JournalsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Journal item) {
                LogUtils.showLog(LOG_TAG, "@all journals::" + item);
                mPresenter.openJournalDetails(item);

            }
        }, getActivity());

        mBinding.list.setLayoutManager(new LinearLayoutManager(getActivity()));

        mBinding.list.setHasFixedSize(true);
        mBinding.list.setAdapter(mListAdapter);
        mListAdapter.notifyItemChanged(mJournalList.size() - 1);


    }

    @Override
    public void showAddJournal(@NonNull String userId) {
        Intent intent = new Intent(getContext(), AddEditJournalActivity.class);
        intent.putExtra(ARGUMENT_USER_ID, userId);
        startActivityForResult(intent, AddEditJournalActivity.REQUEST_ADD_JOURNAL);
    }

    @Override
    public void showJournalDetailsUi(long journalId, String userId) {
        LogUtils.showLog(LOG_TAG, "@Fetched journalId" + journalId);

        Intent intent = new Intent(getContext(), JournalDetailActivity.class);
        intent.putExtra(JournalDetailActivity.EXTRA_JOURNAL_ID, journalId);
        intent.putExtra(JournalDetailActivity.EXTRA_USER_ID, userId);
        startActivity(intent);
    }

    @Override
    public void showLoadingJournalsError() {
        String errorMessage = getString(R.string.error_loading_journal);
        showMessage(errorMessage);
    }

    @Override
    public void showNoJournals() {
        String noJournalsMessage = getString(R.string.no_journals);
        showNoJournal(noJournalsMessage);
    }

    @Override
    public boolean viewIsActive() {
        return isAdded();
    }

    @Override
    public void showSuccessFullySaved() {
        showMessage(getString(R.string.successfully_saved_journal_message));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.result(requestCode, resultCode);
    }

    @Override
    public void setPresenter(JournalsContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }


    private void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    private void showNoJournal(String mainText) {
        mBinding.list.setVisibility(View.GONE);
        mBinding.tvNoMessage.setVisibility(View.VISIBLE);
        mBinding.imgNoJournal.setVisibility(View.VISIBLE);
        mBinding.tvNoMessage.setText(mainText);
        mBinding.imgNoJournal.setImageResource(R.drawable.ic_local_library_white_24dp);
    }


}
