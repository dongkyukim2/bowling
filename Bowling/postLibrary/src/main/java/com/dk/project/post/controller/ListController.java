package com.dk.project.post.controller;

import com.dk.project.post.model.MediaSelectListModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by dkkim on 2017-10-05.
 */

public class ListController {

    private static ListController instance;

    private HashMap<String, MediaSelectListModel> mediaSelectMap;
    private ArrayList<MediaSelectListModel> mediaSelectList;


    private ListController() {
        initSelectList();
    }

    public static ListController getInstance() {
        if (instance == null) {
            instance = new ListController();
        }
        return instance;
    }

    public void addMediaSelectModel(MediaSelectListModel model) {
        mediaSelectList.add(model);
    }

    public void removeMediaSelectModel(MediaSelectListModel model) {
        mediaSelectList.remove(model);
    }

    public boolean isExistModel(String path) {
        return mediaSelectMap.containsKey(path);
    }

    public int getSelectListCount() {
        return mediaSelectList.size();
    }

    public void mediaSelectListClear() {
        mediaSelectList.clear();
    }

    public ArrayList<MediaSelectListModel> getMediaSelectList() {
        return mediaSelectList;
    }

    public void setMediaIndex(String path, int index) {
        mediaSelectMap.get(path).setSort(index);
    }

    public int getIndex(String path) {
        return mediaSelectMap.get(path).getSort();
    }

    private void initSelectList() {
        mediaSelectMap = new HashMap<>();
        mediaSelectList = new ArrayList<MediaSelectListModel>() {
            @Override
            public boolean add(MediaSelectListModel mediaAttachListItem) {
                if (!mediaSelectMap.containsKey(mediaAttachListItem.getFilePath())) {
                    mediaSelectMap.put(mediaAttachListItem.getFilePath(), mediaAttachListItem);
                    super.add(mediaAttachListItem);
                    return true;
                }
                return false;
            }

            @Override
            public void add(int index, MediaSelectListModel element) {
                if (!mediaSelectMap.containsKey(element.getFilePath())) {
                    mediaSelectMap.put(element.getFilePath(), element);
                    super.add(index, element);
                }
                return;
            }

            @Override
            public boolean remove(Object o) {
                MediaSelectListModel item = mediaSelectMap.remove(((MediaSelectListModel) o).getFilePath());

                if (super.remove(item)) {
                    mediaSelectMap.remove(item.getFilePath());
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public boolean addAll(Collection<? extends MediaSelectListModel> c) {
                for (MediaSelectListModel item : c) {
                    mediaSelectMap.put(item.getFilePath(), item);
                }
                return super.addAll(c);
            }

            @Override
            public MediaSelectListModel remove(int index) {
                MediaSelectListModel item = super.remove(index);
                mediaSelectMap.remove(item.getFilePath());
                return item;
            }

            @Override
            public void clear() {
                mediaSelectMap.clear();
                super.clear();
            }
        };
    }
}
