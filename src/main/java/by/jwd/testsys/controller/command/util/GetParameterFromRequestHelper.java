package by.jwd.testsys.controller.command.util;

import by.jwd.testsys.bean.Type;
import by.jwd.testsys.controller.parameter.RequestParameterName;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class GetParameterFromRequestHelper {


    public static int getCurrentPage(HttpServletRequest request) {

        String currentPage = request.getParameter(RequestParameterName.CURRENT_PAGE);
        int page = 1;
        if (currentPage != null) {
            page = Integer.parseInt(currentPage);
        }

        return page;
    }

    public static int getActiveTypeId(HttpServletRequest request, List<Type> types) {
        String typeId = request.getParameter(RequestParameterName.TEST_TYPE_ID);
        int activeTypeId = 0;
        if (typeId != null) {
            activeTypeId = Integer.parseInt(typeId);
        } else {
            activeTypeId = types.get(0).getId();
        }
        return activeTypeId;
    }
}
