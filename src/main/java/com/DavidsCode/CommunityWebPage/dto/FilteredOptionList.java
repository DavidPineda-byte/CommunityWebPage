package com.DavidsCode.CommunityWebPage.dto;

import java.util.ArrayList;

public class FilteredOptionList {


    private final ArrayList<Option> filteredOptionList;

    private final ArrayList<Option> AllOptionList;

    private final String filterBy;
        // Constructor
    public FilteredOptionList(ArrayList<Option> AllOptionList, String input){
        this.AllOptionList = AllOptionList;
        this.filterBy = input;
        this.filteredOptionList = FilterOptionList(AllOptionList, filterBy);
    }
     // method to filter Options
    public ArrayList<Option> FilterOptionList(ArrayList<Option> AllOptionList, String filterBy){

        ArrayList<Option> filteredOptionList = new ArrayList<>();
       for(Option option : AllOptionList){

           if(option.getName().toLowerCase().contains(filterBy.toLowerCase())){
               filteredOptionList.add(option);
           }
       }
       return filteredOptionList;
    }
// getter
    public ArrayList<Option> getFilteredOptionList(){
        return  filteredOptionList;
    }
}
