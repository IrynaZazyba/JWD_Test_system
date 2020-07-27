package by.jwd.testsys.controller.command.ajax;

import by.jwd.testsys.bean.Role;

public enum AjaxCommandName {
    EDIT_USER(Role.USER), SHOW_QUESTION(Role.USER), SAVE_ANSWER(Role.USER), GET_TESTS(Role.USER), ASSIGN_TEST(Role.ADMIN),
    GET_ASSIGNED_USERS(Role.ADMIN), DELETE_ASSIGNMENT(Role.ADMIN),SHOW_RESULT_DATA(Role.USER), DELETE_TEST(Role.ADMIN),
    CREATE_TEST(Role.ADMIN),CREATE_QUESTION_ANSWER(Role.ADMIN),UPDATE_QUESTION(Role.ADMIN),COMPLETE_TEST(Role.ADMIN),
    DELETE_QUESTION(Role.ADMIN),UPDATE_TEST_INFO(Role.ADMIN),ADD_TEST_TYPE(Role.ADMIN), CHANGE_PASSWORD(Role.USER),
    NO_COMMAND(Role.USER),DELETE_TYPE(Role.ADMIN);

    private Role role;

    AjaxCommandName(Role role){
        this.role=role;
    }

    public Role getRole(){
        return role;
    }
}
