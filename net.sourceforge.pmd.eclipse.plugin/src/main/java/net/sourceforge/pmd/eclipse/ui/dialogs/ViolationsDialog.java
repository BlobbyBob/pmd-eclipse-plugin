package net.sourceforge.pmd.eclipse.ui.dialogs;

import net.sourceforge.pmd.Report;
import net.sourceforge.pmd.RuleViolation;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IconAndMessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class ViolationsDialog extends IconAndMessageDialog {

    private ScrolledComposite detailsArea;
    private Report report;

    public ViolationsDialog(Shell parentShell) {
        super(parentShell);
        int count = 0;
        for (RuleViolation violation : report)
            if (!violation.isSuppressed())
                count++;
        message = count + " violations found.";
    }

    protected Image getImage() {
        return getWarningImage();
    }


    @Override
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);
        GridLayout layout = new GridLayout(1, false);
        layout.marginRight = 5;
        layout.marginLeft = 10;
        container.setLayout(layout);

        createMessageArea(container);

        detailsArea = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
        GridLayout detailsLayout = new GridLayout(2, false);
        detailsLayout.marginBottom = 10;
        detailsLayout.marginRight = 5;
        detailsLayout.marginLeft = 5;
        detailsArea.setLayout(detailsLayout);
        detailsArea.setVisible(false);

        for (RuleViolation violation : report) {
            if (violation.isSuppressed()) continue;
            Label rulename = new Label(detailsArea, SWT.NONE);
            rulename.setText(violation.getRule().getName());
            Label description = new Label(detailsArea, SWT.NONE);
            description.setText(violation.getDescription());
        }

        return container;
    }

    @Override
    protected Control createMessageArea(Composite composite) {
        return super.createMessageArea(composite);
    }

    // Detail button has been pressed
    private void detailsPressed() {
        detailsArea.setVisible(!detailsArea.getVisible());
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, false);
        createButton(parent, IDialogConstants.DETAILS_ID, IDialogConstants.SHOW_DETAILS_LABEL, true);
    }

    @Override
    protected void buttonPressed(int buttonId) {
        if (buttonId == IDialogConstants.DETAILS_ID) {
            detailsPressed();
        } else {
            super.buttonPressed(buttonId);
        }
    }

    @Override
    protected Point getInitialSize() {
        return new Point(450, 300);
    }


    public void setReport(Report report) {
        this.report = report;
    }
}