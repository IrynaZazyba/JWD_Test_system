package by.jwd.testsys.presentation;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

public class ResultTag extends TagSupport {

    private static final long serialVersionUID = -5845012029025545470L;

    private int rightCountQuestion;
    private int countTestQuestion;

    @Override
    public int doStartTag() throws JspException {

        JspWriter out = pageContext.getOut();
        try {
            double result =(rightCountQuestion * 100) / countTestQuestion;
            out.write(String.format("%.2f",result));
        } catch (IOException e) {
            throw new JspException(e);
        }
        return SKIP_BODY;
    }

    public int getRightCountQuestion() {
        return rightCountQuestion;
    }

    public void setRightCountQuestion(int rightCountQuestion) {
        this.rightCountQuestion = rightCountQuestion;
    }

    public int getCountTestQuestion() {
        return countTestQuestion;
    }

    public void setCountTestQuestion(int countTestQuestion) {
        this.countTestQuestion = countTestQuestion;
    }
}
