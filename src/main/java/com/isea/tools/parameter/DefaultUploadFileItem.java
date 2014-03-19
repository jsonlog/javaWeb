package com.isea.tools.parameter;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liuzh on 14-3-17.
 */
public class DefaultUploadFileItem implements UploadFileItem {

    @Override
    public Map<String, List<Object>> getFieldObjects(HttpServletRequest request) {
        Map<String, List<Object>> fieldMap = new HashMap<String, List<Object>>();
        DiskFileItemFactory factory = new DiskFileItemFactory();

        ServletContext servletContext = request.getSession().getServletContext();
        File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
        factory.setRepository(repository);

        ServletFileUpload upload = new ServletFileUpload(factory);
        List<Object> results = new ArrayList<Object>();
        try {
            List<FileItem> items = upload.parseRequest(request);
            for (FileItem item : items) {
                String fname = item.getFieldName();
                if(fieldMap.get(fname)==null){
                    fieldMap.put(fname,new ArrayList<Object>());
                }
                if (item.isFormField()) {
                    fieldMap.get(fname).add(item.getString());
                } else {
                    fieldMap.get(fname).add(item);
                }
            }
        } catch (FileUploadException e) {
            e.printStackTrace();
        }
        return fieldMap;
    }
}
