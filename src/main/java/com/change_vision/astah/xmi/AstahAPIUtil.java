package com.change_vision.astah.xmi;

import com.change_vision.astah.xmi.exception.IllegalUseByCommunityEditionException;
import com.change_vision.jude.api.inf.editor.BasicModelEditor;
import com.change_vision.jude.api.inf.editor.IModelEditorFactory;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException;
import com.change_vision.jude.api.inf.model.IModel;
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
    
    public IModel getProject() {
        try {
            return getProjectAccessor().getProject();
        } catch (ProjectNotFoundException e) {
            throw new IllegalStateException("Project is not found.",e);
        }
    }
    
    public BasicModelEditor getBasicModelEditor(){
        try {
            return getModelEditorFactory().getBasicModelEditor();
        } catch (InvalidEditingException e) {
            throw new IllegalUseByCommunityEditionException();
        }
    }

    private IModelEditorFactory getModelEditorFactory() {
        return getProjectAccessor().getModelEditorFactory();
    }

}
