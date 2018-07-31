package com.ek.email.learnsqlite;

import java.util.List;

public interface SignUpListener {
    void didDatasList(int id , List<String> name_list, List<String> username_list, List<String> pass_list, List<String> notes_list, List<String> photo_list);
    void addDatasToDb();
}
