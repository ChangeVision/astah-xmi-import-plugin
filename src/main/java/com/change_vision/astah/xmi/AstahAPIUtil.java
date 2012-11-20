package com.change_vision.astah.xmi;

import com.change_vision.astah.xmi.exception.IllegalUseByCommunityEditionException;
import com.change_vision.jude.api.inf.editor.BasicModelEditor;
import com.change_vision.jude.api.inf.editor.IModelEditorFactory;
import com.change_vision.jude.api.inf.editor.UseCaseModelEditor;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.project.ProjectAccessor;
import com.change_vision.jude.api.inf.project.ProjectAccessorFactory;

public class AstahAPIUtil {
    
    public ProjectAccessor getProjectAccessor(){
        try {
            return ProjectAccessorFactory.getProjectAccessor();
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("ProjectAccessor is not found. It may be CLASSPATH issue.");
        }
    }
    
    public BasicModelEditor getBasicModelEditor(){
        try {
            return getModelEditorFactory().getBasicModelEditor();
        } catch (InvalidEditingException e) {
            throw new IllegalUseByCommunityEditionException();
        }
    }

    public UseCaseModelEditor getUseCaseModelEditor(){
        try {
            return getModelEditorFactory().getUseCaseModelEditor();
        } catch (InvalidEditingException e) {
            throw new IllegalUseByCommunityEditionException();
        }        
    }

    private IModelEditorFactory getModelEditorFactory() {
        return getProjectAccessor().getModelEditorFactory();
    }

}
