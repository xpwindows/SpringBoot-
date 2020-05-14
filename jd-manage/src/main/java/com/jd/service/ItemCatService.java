package com.jd.service;

import com.jd.vo.EasyUI_Tree;

import java.util.List;

public interface ItemCatService {
    //根据分类id查询分类名称
    String findItemCatNameById(Long itemCatId);
    //通过父id查询下级目录结构
    List<EasyUI_Tree> findItemCatByParentId(Long parentId);
    List<EasyUI_Tree> findItemCatByCache(Long parentId);
}
