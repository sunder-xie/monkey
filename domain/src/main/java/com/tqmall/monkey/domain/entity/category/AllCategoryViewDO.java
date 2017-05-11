package com.tqmall.monkey.domain.entity.category;

public class AllCategoryViewDO {
    private String vehicleCode;

    private Short firstCatId;

    private String firstCatName;

    private String firstCatCode;

    private Short secondCatId;

    private String secondCatName;

    private String secondCatCode;

    private Short thirdCatId;

    private String thirdCatName;

    private String thirdCatCode;

    private Integer partId;

    private Integer cateId;

    private String partName;

    private String partCode;

    private String sumCode;

    private String labelNameText;

    private String alissNameText;

    private String firstCatDeleted;
    private String secondCatDeleted;
    private String thirdCatDeleted;
    private String partDeleted;
    private String partKind;

    //系统临时变量
    private String isDeleted;
    private Integer seqNumber;


    public String getVehicleCode() {
        return vehicleCode;
    }

    public void setVehicleCode(String vehicleCode) {
        this.vehicleCode = vehicleCode == null ? null : vehicleCode.trim();
    }

    public Short getFirstCatId() {
        return firstCatId;
    }

    public void setFirstCatId(Short firstCatId) {
        this.firstCatId = firstCatId;
    }

    public String getFirstCatName() {
        return firstCatName;
    }

    public void setFirstCatName(String firstCatName) {
        this.firstCatName = firstCatName == null ? null : firstCatName.trim();
    }

    public String getFirstCatCode() {
        return firstCatCode;
    }

    public void setFirstCatCode(String firstCatCode) {
        this.firstCatCode = firstCatCode == null ? null : firstCatCode.trim();
    }

    public Short getSecondCatId() {
        return secondCatId;
    }

    public void setSecondCatId(Short secondCatId) {
        this.secondCatId = secondCatId;
    }

    public String getSecondCatName() {
        return secondCatName;
    }

    public void setSecondCatName(String secondCatName) {
        this.secondCatName = secondCatName == null ? null : secondCatName.trim();
    }

    public String getSecondCatCode() {
        return secondCatCode;
    }

    public void setSecondCatCode(String secondCatCode) {
        this.secondCatCode = secondCatCode == null ? null : secondCatCode.trim();
    }

    public Short getThirdCatId() {
        return thirdCatId;
    }

    public void setThirdCatId(Short thirdCatId) {
        this.thirdCatId = thirdCatId;
    }

    public String getThirdCatName() {
        return thirdCatName;
    }

    public void setThirdCatName(String thirdCatName) {
        this.thirdCatName = thirdCatName == null ? null : thirdCatName.trim();
    }

    public String getThirdCatCode() {
        return thirdCatCode;
    }

    public void setThirdCatCode(String thirdCatCode) {
        this.thirdCatCode = thirdCatCode == null ? null : thirdCatCode.trim();
    }

    public Integer getPartId() {
        return partId;
    }

    public void setPartId(Integer partId) {
        this.partId = partId;
    }

    public Integer getCateId() {
        return cateId;
    }

    public void setCateId(Integer cateId) {
        this.cateId = cateId;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName == null ? null : partName.trim();
    }

    public String getPartCode() {
        return partCode;
    }

    public void setPartCode(String partCode) {
        this.partCode = partCode == null ? null : partCode.trim();
    }

    public String getSumCode() {
        return sumCode;
    }

    public void setSumCode(String sumCode) {
        this.sumCode = sumCode == null ? null : sumCode.trim();
    }

    public String getLabelNameText() {
        return labelNameText;
    }

    public void setLabelNameText(String labelNameText) {
        this.labelNameText = labelNameText == null ? null : labelNameText.trim();
    }

    public String getAlissNameText() {
        return alissNameText;
    }

    public void setAlissNameText(String alissNameText) {
        this.alissNameText = alissNameText == null ? null : alissNameText.trim();
    }

    public String getFirstCatDeleted() {
        return firstCatDeleted;
    }

    public void setFirstCatDeleted(String firstCatDeleted) {
        this.firstCatDeleted = firstCatDeleted;
    }

    public String getSecondCatDeleted() {
        return secondCatDeleted;
    }

    public void setSecondCatDeleted(String secondCatDeleted) {
        this.secondCatDeleted = secondCatDeleted;
    }

    public String getThirdCatDeleted() {
        return thirdCatDeleted;
    }

    public void setThirdCatDeleted(String thirdCatDeleted) {
        this.thirdCatDeleted = thirdCatDeleted;
    }

    public String getPartDeleted() {
        return partDeleted;
    }

    public void setPartDeleted(String partDeleted) {
        this.partDeleted = partDeleted;
    }

    public String getDeletedStatus(){
        String str = CategoryDO.IS_DELETED_TRUE;
        if(str.equals(this.partDeleted)){
            return "停用";
        }

        if(str.equals(this.thirdCatDeleted)){
            return "停用";
        }

        if(str.equals(this.secondCatDeleted)){
            return "停用";
        }

        if(str.equals(this.firstCatDeleted)){
            return "停用";
        }

        return "";
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Integer getSeqNumber() {
        return seqNumber;
    }

    public void setSeqNumber(Integer seqNumber) {
        this.seqNumber = seqNumber;
    }

    public String getPartKind() {
        return partKind;
    }

    public void setPartKind(String partKind) {
        this.partKind = partKind;
    }
}