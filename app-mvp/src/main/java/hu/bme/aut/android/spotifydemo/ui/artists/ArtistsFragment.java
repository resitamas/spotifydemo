package hu.bme.aut.android.spotifydemo.ui.artists;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import hu.bme.aut.android.spotifydemo.R;
import hu.bme.aut.android.spotifydemo.model.Item;
import hu.bme.aut.android.spotifydemo.ui.main.MainActivity;

/**
 * A placeholder fragment containing a simple screen.
 */
public class ArtistsFragment extends Fragment
        implements ArtistsScreen {

    @BindView(R.id.etArtist) public EditText etArtist;
    @BindView(R.id.recyclerViewArtists) public RecyclerView recyclerViewArtists;
    @BindView(R.id.swipeRefreshLayoutArtists) private SwipeRefreshLayout swipeRefreshLayoutArtists;
    @BindView(R.id.tvEmpty) public TextView tvEmpty;

    private Unbinder mUnbinder;

    private List<Item> artistsList;

    private ArtistsAdapter artistsAdapter;

    private String artist = "queen";

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);

        artist = getActivity().getIntent().getStringExtra(MainActivity.KEY_ARTIST);
        ArtistsPresenter.getInstance().attachScreen(this);
    }

    @Override
    public void onDetach() {
        ArtistsPresenter.getInstance().detachScreen();
        super.onDetach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artists, container, false);

        mUnbinder = ButterKnife.bind(this, view);

        etArtist.setText(artist);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewArtists.setLayoutManager(llm);

        artistsList = new ArrayList<>();
        artistsAdapter = new ArtistsAdapter(getContext(), artistsList);
        recyclerViewArtists.setAdapter(artistsAdapter);

        swipeRefreshLayoutArtists.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                artist = etArtist.getText().toString();
                ArtistsPresenter.getInstance().refreshArtists(
                        artist);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ArtistsPresenter.getInstance().getPrevResult() == null) {
            ArtistsPresenter.getInstance().refreshArtists(artist);
        } else {
            showArtists(ArtistsPresenter.getInstance().getPrevResult());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void showNetworkError(String errorMsg) {
        if (swipeRefreshLayoutArtists != null) {
            swipeRefreshLayoutArtists.setRefreshing(false);
        }
        Toast.makeText(getContext(), errorMsg, Toast.LENGTH_LONG).show();
    }

    public void showArtists(List<Item> artists) {
        if (swipeRefreshLayoutArtists != null) {
            swipeRefreshLayoutArtists.setRefreshing(false);
        }

        artistsList.clear();
        artistsList.addAll(artists);
        artistsAdapter.notifyDataSetChanged();

        if (artistsList.isEmpty()) {
            recyclerViewArtists.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            recyclerViewArtists.setVisibility(View.VISIBLE);
            tvEmpty.setVisibility(View.GONE);
        }
    }
}
