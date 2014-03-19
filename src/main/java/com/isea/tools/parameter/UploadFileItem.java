package com.isea.tools.parameter;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by liuzh on 14-3-17.
 */
public interface UploadFileItem {
    Map<String,List<Object>> getFieldObjects(HttpServletRequest request);
}
