package com.dk.project.bowling.ui.fragment;


import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.dk.project.bowling.R;
import com.dk.project.post.base.BindFragment;
import com.dk.project.bowling.databinding.FragmentGraphBinding;
import com.dk.project.bowling.ui.adapter.GraphScoreAdapter;
import com.dk.project.bowling.viewModel.GraphViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class GraphFragment extends BindFragment<FragmentGraphBinding, GraphViewModel> {


  private LinearLayoutManager linearLayoutManager;
  private GraphScoreAdapter graphScoreAdapter;


  public static GraphFragment newInstance() {
    GraphFragment graphFragment = new GraphFragment();
    return graphFragment;
  }

  @Override
  protected int getLayoutId() {
    return R.layout.fragment_graph;
  }

  @Override
  protected GraphViewModel createViewModel() {
    return ViewModelProviders.of(this).get(GraphViewModel.class);
  }

  @Override
  protected void registerLiveData() {
    viewModel.getViewTypeLiveData().observe(this, type -> {
      switch (type) {
        case 0:
          binding.graphViewType.setText("평균점수");
          break;
        case 1:
          binding.graphViewType.setText("최대점수");
          break;
        case 2:
          binding.graphViewType.setText("최소점수");
          break;
      }
    });

    viewModel.getGraphLiveData().observe(this, scoreModels -> {
      binding.graphDate.setText(viewModel.getYearMonth());
      graphScoreAdapter.setGraphScoreList(scoreModels);
    });
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    binding.setViewModel(getViewModel());

    graphScoreAdapter = new GraphScoreAdapter();

    linearLayoutManager = new LinearLayoutManager(getActivity());
    linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
    binding.graphRecycler.setLayoutManager(linearLayoutManager);
    binding.graphRecycler.setItemAnimator(new DefaultItemAnimator());
    binding.graphRecycler.setAdapter(graphScoreAdapter);

    super.onViewCreated(view, savedInstanceState);

  }
}
