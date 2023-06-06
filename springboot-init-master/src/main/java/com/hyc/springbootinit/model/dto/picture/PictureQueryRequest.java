package com.hyc.springbootinit.model.dto.picture;

import com.hyc.springbootinit.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class PictureQueryRequest extends PageRequest implements Serializable {

    /**
     * 检索文本
     */
    private String searchText;

    private static final long serialVersionId = 1L;


}
