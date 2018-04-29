package com.simple.service;

import com.simple.common.ServerResponse;
import com.simple.pojo.Category;

import java.util.List;

/**
 * Create by S I M P L E on 2017/09/21
 */
public interface ICategoryService {
    ServerResponse addCategory(String categoryName, Integer parentId);

    ServerResponse updateCategoryName(Integer categoryId, String categoryName);

    ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId);

    ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId);
}
