/*
 * Copyright 2015 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.anstis.client.grid.widget.edit;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import org.anstis.client.grid.model.BaseGridCellValue;
import org.anstis.client.grid.model.ICallback;
import org.anstis.client.grid.model.IGridCellValue;
import org.gwtbootstrap3.client.shared.event.ModalShownEvent;
import org.gwtbootstrap3.client.shared.event.ModalShownHandler;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;
import org.gwtbootstrap3.client.ui.ModalFooter;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;

/**
 * A simple popup to demonstrate "out of cell" editing for Strings.
 */
public class EditorPopup extends Modal {

    private final TextBox textBox = new TextBox();

    private IGridCellValue<String> value;
    private ICallback<IGridCellValue<String>> callback = null;

    public EditorPopup() {
        setTitle( "Edit" );
        setFade( true );

        textBox.addKeyDownHandler( new KeyDownHandler() {
            @Override
            public void onKeyDown( final KeyDownEvent event ) {
                if ( event.getNativeKeyCode() == KeyCodes.KEY_ENTER ) {
                    commit();
                }
            }
        } );

        final ModalBody body = new ModalBody();
        body.add( textBox );
        add( body );

        final ModalFooter footer = new ModalFooter();
        final Button okButton = new Button( "OK" );
        okButton.setIcon( IconType.EDIT );
        okButton.setType( ButtonType.PRIMARY );
        okButton.addClickHandler( new ClickHandler() {
            @Override
            public void onClick( final ClickEvent event ) {
                commit();
            }
        } );

        final Button cancelButton = new Button( "Cancel" );
        cancelButton.addClickHandler( new ClickHandler() {
            @Override
            public void onClick( final ClickEvent event ) {
                cancel();
            }
        } );

        footer.add( okButton );
        footer.add( cancelButton );
        add( footer );

        addShownHandler( new ModalShownHandler() {
            @Override
            public void onShown( final ModalShownEvent evt ) {
                textBox.setFocus( true );
            }
        } );
    }

    /**
     * Show the popup
     * @param value The value to show in the editor.
     * @param callback Callback to invoke when the popup is "OK'ed".
     */
    public void edit( final IGridCellValue<String> value,
                      final ICallback<IGridCellValue<String>> callback ) {
        this.value = value;
        this.callback = callback;
        textBox.setText( value == null ? "" : value.getValue() );
        show();
    }

    private void cancel() {
        hide();
    }

    private void commit() {
        if ( callback != null ) {
            callback.callback( new BaseGridCellValue<>( textBox.getText() ) );
        }
        hide();
    }

}
