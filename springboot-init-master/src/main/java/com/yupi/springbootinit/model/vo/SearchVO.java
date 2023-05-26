package com.yupi.springbootinit.model.vo;

import com.yupi.springbootinit.model.entity.Picture;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author hyc
 * 聚合搜索返回vo对象
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SearchVO implements Serializable {

    private List<Picture> pictureList;

    private List<UserVO> userList;

    private List<PostVO> postList;

    private List<?> dataList;

    private static final long serialVersionUID = 1L;

}
