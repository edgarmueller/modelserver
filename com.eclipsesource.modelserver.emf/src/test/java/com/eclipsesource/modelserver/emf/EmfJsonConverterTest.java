package com.eclipsesource.modelserver.emf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.BasicConfigurator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class EmfJsonConverterTest extends AbstractResourceTest {

	private EmfJsonConverter emfJsonConverter;

	private String simpleTest_json;
	private EClass simpleTest_eClass;
	private String coffee_json;
	private Resource coffee_resource;

	@BeforeClass
	public static void temp() {
		BasicConfigurator.configure();
	}

	@Before
	public void setUp() throws IOException {
		this.emfJsonConverter = new EmfJsonConverter();
		simpleTest_json = "{\n" + "  \"eClass\" : \"http://www.eclipse.org/emf/2002/Ecore#//EClass\",\n"
				+ "  \"name\" : \"SimpleTest\"\n" + "}";

		simpleTest_eClass = EcoreFactory.eINSTANCE.createEClass();
		simpleTest_eClass.setName("SimpleTest");
		coffee_json = FileUtils.readFileToString(new File(RESOURCE_PATH + "/" + "Coffee.json"), "UTF8");

		coffee_resource = loadResource("Coffee.ecore");
	}

	@Test
	public void testToJson_simple() {
		String expected = simpleTest_json;
		Optional<String> result = emfJsonConverter.toJson(simpleTest_eClass);
		assertTrue(result.isPresent());
		assertEquals(result.get(), expected);
	}

	@Test
	public void testFromJson_simple() {
		EObject expected = simpleTest_eClass;
		Optional<EObject> result = emfJsonConverter.fromJson(simpleTest_json);
		assertTrue(result.isPresent());
		assertTrue(EcoreUtil.equals(expected, result.get()));
	}

	@Test
	public void testFromJson_explicitCast() {
		Optional<EPackage> result = emfJsonConverter.fromJson(coffee_json, EPackage.class);
		assertTrue(result.isPresent());
		assertTrue(EPackage.class.isInstance(result.get()));

	}

	@Test
	public void testFromJson_failedCast() {
		Optional<EEnumLiteral> result = emfJsonConverter.fromJson(coffee_json, EEnumLiteral.class);
		assertFalse(result.isPresent());

	}

	@Test
	public void testToJson_coffee() {
		String expected = coffee_json;
		Optional<String> result = emfJsonConverter.toJson(coffee_resource.getContents().get(0));
		assertTrue(result.isPresent());
		assertEquals(result.get(), expected);

	}

	@Test
	public void testFromJson_coffee() {
		EObject expected = coffee_resource.getContents().get(0);
		Optional<EObject> result = emfJsonConverter.fromJson(coffee_json);
		assertTrue(result.isPresent());
		System.out.println(result.get().toString());
		System.out.println(expected.toString());
		assertTrue(quickIsEqual(expected, result.get()));

	}

	private boolean quickIsEqual(EObject pack1, EObject pack2) {
		if (!pack1.getClass().equals(pack2.getClass())) {
			return false;
		}

		if (pack1 instanceof ENamedElement) {
			if (!((ENamedElement) pack1).getName().equals(((ENamedElement) pack2).getName())) {
				return false;
			}

		}

		if (pack1.eContents().size() != pack2.eContents().size()) {
			return false;
		}

		return true;
	}

}
