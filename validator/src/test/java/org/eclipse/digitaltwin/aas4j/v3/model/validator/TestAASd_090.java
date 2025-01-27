/*
 * Copyright (c) 2021 Fraunhofer-Gesellschaft zur Foerderung der angewandten Forschung e. V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.eclipse.digitaltwin.aas4j.v3.model.validator;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.digitaltwin.aas4j.v3.model.Blob;
import org.eclipse.digitaltwin.aas4j.v3.model.DataTypeDefXsd;
import org.eclipse.digitaltwin.aas4j.v3.model.File;
import org.eclipse.digitaltwin.aas4j.v3.model.Property;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultBlob;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultFile;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultProperty;
import org.junit.Test;

/**
 * Tests the following constraint:
 * <p>
 * <i>For data elements DataElement/category shall be one of the following
 * values: CONSTANT, PARAMETER or VARIABLE. Exception: File and Blob data
 * elements. </i>
 * </p>
 * 
 * @author schnicke
 *
 */
public class TestAASd_090 {
	@Test
	public void correctCategoryConstant() throws ValidationException {
		ShaclValidator.getInstance().validate(createProperty("idShort1", "CONSTANT"));
	}

	@Test
	public void correctCategoryParameter() throws ValidationException {
		ShaclValidator.getInstance().validate(createProperty("idShort1", "PARAMETER"));
	}

	@Test
	public void correctCategoryVariable() throws ValidationException {
		ShaclValidator.getInstance().validate(createProperty("idShort1", "VARIABLE"));
	}

	@Test
	public void fileCategoryDoesntMatter() throws ValidationException {
		ShaclValidator.getInstance().validate(createFile());
	}

	@Test
	public void blobCategoryDoesntMatter() throws ValidationException {
		ShaclValidator.getInstance().validate(createBlob());
	}

	@Test
	public void wrongCategory() {
		try {
			ShaclValidator.getInstance().validate(createProperty("idShort1", "WRONG"));
			fail();
		} catch (ValidationException e) {
			assertTrue(e.getMessage().endsWith(
					"For data elements Referable/category shall be one of the following values: CONSTANT, PARAMETER or VARIABLE. Exception: File and Blob data elements."));
		}
	}

	private Property createProperty(String idShort, String category) {
		return new DefaultProperty.Builder()
				.idShort(idShort)
				.category(category)
				.value("test")
				.valueType(DataTypeDefXsd.STRING)
				.build();
	}

	private File createFile() {
		return new DefaultFile.Builder()
				.idShort("idShort1")
				.category("DOESNTMATTER")
				.contentType("application/json").value("file:///test.json")
				.build();
	}
	
	private Blob createBlob() {
		return new DefaultBlob.Builder()
				.idShort("idShort2")
				.category("DOESNTMATTER")
				.contentType("application/json")
				.value(new byte[] { 0, 1, 2 })
				.build();
	}

}
