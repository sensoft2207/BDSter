package com.mxi.buildster.model;

/**
 * Created by vishal on 14/5/18.
 */

public class ProjectData {

    int id;
    String project_name;
    String project_address;
    String project_manager;
    String company_name;
    String image_path;

    byte[] selectedImage;

    public byte[] getSelectedImage() {
        return selectedImage;
    }

    public void setSelectedImage(byte[] selectedImage) {
        this.selectedImage = selectedImage;
    }

    public ProjectData(String project_name, String project_address, String project_manager,
                       String company_name, String path,byte[] selectedImage) {

        this.project_name = project_name;
        this.project_address = project_address;
        this.project_manager = project_manager;
        this.company_name = company_name;
        this.image_path = path;
        this.selectedImage = selectedImage;
    }

    public ProjectData() {

    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public String getProject_address() {
        return project_address;
    }

    public void setProject_address(String project_address) {
        this.project_address = project_address;
    }

    public String getProject_manager() {
        return project_manager;
    }

    public void setProject_manager(String project_manager) {
        this.project_manager = project_manager;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
