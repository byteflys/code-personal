package com.easing.commons.android.value.measure;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor(suppressConstructorProperties = true)
public class Size {

    public int w;
    public int h;
}
