package com.hyc.springbootinit.model.dto.search;

import com.hyc.springbootinit.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author hyc
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SearchAllRequest extends PageRequest implements Serializable {

    /**
     * 搜索词
     */
    private String searchText;

    /**
     * 搜索类型
     */
    private String type;

    private static final long serialVersionID = 1L;

}
