package by.jwd.testsys.controller.parameter;

import by.jwd.testsys.bean.Type;
import by.jwd.testsys.controller.parameter.RequestParameterName;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class GetParameterFromRequestHelper {

    private static final String BOOLEAN_PARAMETER_TRUE = "true";
    private static final String BOOLEAN_PARAMETER_ON = "on";
    private static final String BOOLEAN_PARAMETER_YES= "on";
    private static final String BOOLEAN_PARAMETER_FALSE = "false";
    private static final String BOOLEAN_PARAMETER_OFF = "off";
    private static final String BOOLEAN_PARAMETER_NO = "no";


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

    public static boolean parseBooleanParameter(HttpServletRequest request, String parameterName) {

        String booleanParam = request.getParameter(parameterName);
        if (booleanParam.equalsIgnoreCase(BOOLEAN_PARAMETER_TRUE) ||
                booleanParam.equalsIgnoreCase(BOOLEAN_PARAMETER_ON) ||
                booleanParam.equalsIgnoreCase(BOOLEAN_PARAMETER_YES)) {
            return true;
        } else if (booleanParam.equalsIgnoreCase(BOOLEAN_PARAMETER_FALSE) ||
                booleanParam.equalsIgnoreCase(BOOLEAN_PARAMETER_OFF) ||
                booleanParam.equalsIgnoreCase(BOOLEAN_PARAMETER_NO)) {
            return false;
        } else {
            throw new NumberFormatException("Parameter " + booleanParam + " is not a boolean");
        }
    }
}
