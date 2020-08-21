package com.application.recylerview_lib;

import java.util.ArrayList;
import java.util.List;

public class ResponseData {
    private List<DataInfo> dataList = new ArrayList<>();

    public List<DataInfo> getDataList() {
        return dataList;
    }

    public void setDataList(List<DataInfo> dataList) {
        this.dataList = dataList;
    }

    public class DataInfo{
        private String code;
        private String name;
        private String description;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        @Override
        public String toString() {
            return "ResponseData{" +
                    "code='" + code + '\'' +
                    ", name='" + name + '\'' +
                    ", description='" + description + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ResponseData{" +
                "dataList=" + dataList +
                '}';
    }
}
