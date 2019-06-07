package com.dk.project.bowling.ui.adapter.callback;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.dk.project.bowling.ui.adapter.CreateGameAdapter;
import com.dk.project.bowling.ui.viewHolder.CreateGameViewHolder;

public class ItemMoveCallback extends ItemTouchHelper.Callback {

    private final CreateGameAdapter mAdapter;

    public ItemMoveCallback(CreateGameAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }


    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        return makeMovementFlags(dragFlags, 0);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        mAdapter.onRowMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {

        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            mAdapter.onDragStart();
        }

        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder instanceof CreateGameViewHolder) {
                CreateGameViewHolder myViewHolder = (CreateGameViewHolder) viewHolder;
                mAdapter.onRowSelected(myViewHolder);
            }
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        if (viewHolder instanceof CreateGameViewHolder) {
            CreateGameViewHolder myViewHolder = (CreateGameViewHolder) viewHolder;
            mAdapter.onRowClear(myViewHolder);
        }
    }

    public interface ItemTouchHelperContract {

        void onBindViewHolder(CreateGameViewHolder holder, int position);

        void onRowMoved(int fromPosition, int toPosition);

        void onRowSelected(CreateGameViewHolder myViewHolder);

        void onRowClear(CreateGameViewHolder myViewHolder);

        void onDragStart();

    }
}