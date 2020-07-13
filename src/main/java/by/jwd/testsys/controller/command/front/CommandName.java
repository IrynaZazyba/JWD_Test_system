package by.jwd.testsys.controller.command.front;

import by.jwd.testsys.bean.Role;

public enum CommandName {

    SIGN_UP(Role.USER),SIGN_IN(Role.USER),SIGN_OUT(Role.USER), SHOW_TESTS_PAGE(Role.USER),SHOW_EXE_TEST_PAGE(Role.USER),
    SHOW_USER_ACCOUNT(Role.USER),CHANGE_LANGUAGE(Role.USER),GET_RESULT(Role.USER),DISPLAY_STATISTIC(Role.USER),
    ASSIGN_TEST(Role.ADMIN),TESTS_RESULTS(Role.ADMIN),SHOW_ADMIN_PANEL(Role.ADMIN), GET_EDIT_TEST_PAGE(Role.ADMIN),
    ADD_TEST(Role.ADMIN), PREVIEW_TEST(Role.ADMIN),EDIT_TEST(Role.ADMIN),SHOW_ASSIGNED_TESTS_PAGE(Role.USER),
    WRONG_REQUEST(Role.USER);

    private Role role;

    CommandName(Role role){
        this.role=role;
    }

    public Role getRole(){
        return role;
    }
}
