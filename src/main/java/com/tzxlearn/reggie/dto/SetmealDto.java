package com.tzxlearn.reggie.dto;


import com.tzxlearn.reggie.domain.Setmeal;
import com.tzxlearn.reggie.domain.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
