package com.change_vision.astah.xmi.internal.convert.model;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.change_vision.astah.xmi.convert.model.UniqueNameCreator;
import com.change_vision.jude.api.inf.model.INamedElement;

public class UniqueNameCreatorTest {
    
    @Mock
    private INamedElement element;

    @Mock
    private INamedElement element0;

    private INamedElement[] elements;

    private UniqueNameCreator creator;

    @Before
    public void before() throws Exception {
        MockitoAnnotations.initMocks(this);
        creator = new UniqueNameCreator();
        elements = new INamedElement[]{};
    }

    @Test
    public void createWithNullNameReturnEmptyString() {
        String uniqueName = creator.getUniqueName(elements, null);
        assertThat(uniqueName,is(""));
    }
    
    @Test
    public void createWithNullElementsReturnName() throws Exception {
        String uniqueName = creator.getUniqueName(null, "hoge");
        assertThat(uniqueName,is("hoge"));        
    }
    
    @Test
    public void withNotExistChildrenReturnName() throws Exception {
        String uniqueName = creator.getUniqueName(elements, "hoge");
        assertThat(uniqueName,is("hoge"));                
    }
    
    @Test
    public void withNotExistSameNameChildrenReturnName() throws Exception {
        elements = new INamedElement[]{
                element,
        };
        when(element.getName()).thenReturn("fuga");
        String uniqueName = creator.getUniqueName(elements, "hoge");
        assertThat(uniqueName,is("hoge"));        
    }
    
    @Test
    public void withExistSameNameChildrenReturnNameZero() throws Exception {
        elements = new INamedElement[]{
                element
        };
        when(element.getName()).thenReturn("hoge");
        String uniqueName = creator.getUniqueName(elements, "hoge");
        assertThat(uniqueName,is("hoge0"));        
    }
    
    @Test
    public void withExistSameNameChildrenReturnNameIncrement() throws Exception {
        elements = new INamedElement[]{
                element,
                element0
        };
        when(element.getName()).thenReturn("hoge");
        when(element0.getName()).thenReturn("hoge0");
        String uniqueName = creator.getUniqueName(elements, "hoge");
        assertThat(uniqueName,is("hoge1"));        
    }

}
