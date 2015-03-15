package com.tsoft.dictionary.client.app.library;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLTable.Cell;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.tsoft.dictionary.client.app.library.callback.BookListRPC;
import com.tsoft.dictionary.client.widget.AbstractPanel;
import com.tsoft.dictionary.server.app.web.library.BookListResponseTO;
import com.tsoft.dictionary.server.app.web.library.BooksFilterRequestTO;
import com.tsoft.dictionary.server.app.web.library.BookListResponseTO.Book;

public class BookListPanel extends AbstractPanel<LibraryForm> implements BookListRPC {
    interface Binder extends UiBinder<Widget, BookListPanel> { }
    private static final Binder binder = GWT.create(Binder.class);
    interface SelectionStyle extends CssResource {
        String selectedRow();
    }
    public static final int VISIBLE_BOOK_COUNT = 25;

    @UiField FlexTable header;
    @UiField FlexTable table;
    @UiField SelectionStyle selectionStyle;

    private NavBar navBar;
    private int startIndex;
    private int selectedRow = -1;
    private int visibleRowCount;

    private BookListResponseTO bookList;

    public BookListPanel() {
        super(binder);
        navBar = new NavBar(this);
    }

    @Override
    protected void onLoad() {
        initTable();
    }

    @Override
    public void beforeBookListRPC(BooksFilterRequestTO requestTO) {
    }

    @Override
    public void afterBookListRPC(BookListResponseTO responseTO, boolean success) {
        if (success) {
            bookList = responseTO;
            update();
        }
    }

    @UiHandler("table")
    void onTableClicked(ClickEvent event) {
        // Select the row that was clicked (-1 to account for header row).
        Cell cell = table.getCellForEvent(event);
        if (cell != null) {
            int row = cell.getRowIndex();
            selectRow(row);
        }
    }

    /**
     * Initializes the table so that it contains enough rows for a full page of
     * emails. Also creates the images that will be used as 'read' flags.
     */
    private void initTable() {
        // Initialize the header
        header.getColumnFormatter().setWidth(0, "128px");
        header.getColumnFormatter().setWidth(1, "256px");
        header.getColumnFormatter().setWidth(3, "64px");
        header.getColumnFormatter().setWidth(4, "64px");

        header.setText(0, 0, "Author(s)");
        header.setText(0, 1, "Name");
        header.setText(0, 2, "Pages");
        header.setText(0, 3, "Year");
        header.setWidget(0, 4, navBar);

        header.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_LEFT);
        header.getCellFormatter().setHorizontalAlignment(2, 3, HasHorizontalAlignment.ALIGN_RIGHT);
        header.getCellFormatter().setHorizontalAlignment(4, 4, HasHorizontalAlignment.ALIGN_RIGHT);

        // Initialize the table
        table.getColumnFormatter().setWidth(0, "128px");
        table.getColumnFormatter().setWidth(1, "256px");
        table.getColumnFormatter().setWidth(2, "64px");
        table.getColumnFormatter().setWidth(3, "64px");
    }

    /**
     * Selects the given row (relative to the current page).
     *
     * @param row the row to be selected
     */
    private void selectRow(int row) {
    }

    /**
     * Navigation Bar support
     * Move back a page
     */
    public void newer() {
        startIndex -= VISIBLE_BOOK_COUNT;
        if (startIndex < 0) {
            startIndex = 0;
        } else {
            styleRow(selectedRow, false);
            selectedRow = -1;
            update();
        }
    }

    /**
     * Navigation Bar support
     * Move forward a page
     */
    public void older() {
        startIndex += VISIBLE_BOOK_COUNT;
        if (startIndex >= bookList.getBooks().size()) {
            startIndex -= VISIBLE_BOOK_COUNT;
        } else {
            styleRow(selectedRow, false);
            selectedRow = -1;
            update();
        }
    }

    private void styleRow(int row, boolean selected) {
        if (row != -1) {
            String style = selectionStyle.selectedRow();

            if (selected) {
                table.getRowFormatter().addStyleName(row, style);
            } else {
                table.getRowFormatter().removeStyleName(row, style);
            }
        }
    }

    private void update() {
        // Update the older/newer buttons & label
        int count = bookList.getBooks().size();
        int max = startIndex + VISIBLE_BOOK_COUNT;
        if (max > count) {
            max = count;
        }

        // Update the nav bar
        navBar.update(startIndex, count, max);

        // Show the selected books
        int bookCount = 0;
        for (int i = 0; i < VISIBLE_BOOK_COUNT && (startIndex + i < count); i ++) {
            Book book = bookList.getBooks().get(startIndex + i);

            // Add a new row to the table, then set each of its columns values
            table.setText(i, 0, book.getAuthor());
            table.setText(i, 1, book.getName());
            table.setText(i, 2, Integer.toString(book.getPageCount()));
            table.setText(i, 3, Integer.toString(book.getPublishedYear()));

            bookCount ++;
        }

        // Clear any remaining slots
        for (int i = bookCount; i < visibleRowCount; i ++) {
            table.removeRow(table.getRowCount() - 1);
        }

        visibleRowCount = bookCount;
    }
}
