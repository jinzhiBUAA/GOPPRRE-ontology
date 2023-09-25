package org.fc.mdt.owl.read;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.jena.ontology.AllValuesFromRestriction;
import org.apache.jena.ontology.AnnotationProperty;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.Restriction;
import org.apache.jena.ontology.SomeValuesFromRestriction;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.util.iterator.ExtendedIterator;

public class GetKARMAModel {

	// 获取语言信息
	public static void getLanguage(String path) {
		// 命名空间（namespace）的URL，用于在本体建模中为实体、属性和其他元素提供唯一的标识符
		String METAG = "http://www.zkhoneycomb.com/formats/metagInOwl#";
		// 创建一个OntModel对象
		OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		// 从指定的文件路径(path)读取本体数据并将其加载到ontModel对象中。
		ontModel.read(path);
		// 将ontModel设置为非严格检测，这样由实例读取类时不会报错
		ontModel.setStrictMode(false);
		// 读取本体的注释属性
		AnnotationProperty localLabel = ontModel.getAnnotationProperty(METAG + "localLabel");
		AnnotationProperty id = ontModel.getAnnotationProperty(METAG + "id");
		// 读取本体中的Language类，即MetaGraph中的语言文件夹
		OntClass language = ontModel.getOntClass(METAG + "Language");
		// 遍历本体中的Language类下的子类，即建模语言
		for (ExtendedIterator<OntClass> it = language.listSubClasses(); it.hasNext();) {
			OntClass ontLanguage = it.next();
			String ontLanguageId = ontLanguage.getPropertyValue(id).toString();
			String ontLanguageName = ontLanguage.getPropertyValue(localLabel).toString();
			System.out.println(ontLanguageId);
			System.out.println(ontLanguageName);
		}
	}

	// 获取属性元模型信息
	public static void getMetaProperty(String path) {
		// 命名空间（namespace）的URL，用于在本体建模中为实体、属性和其他元素提供唯一的标识符
		String METAG = "http://www.zkhoneycomb.com/formats/metagInOwl#";
		// 创建一个OntModel对象
		OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		// 从指定的文件路径(path)读取本体数据并将其加载到ontModel对象中。
		ontModel.read(path);
		// 读取本体的注释属性
		AnnotationProperty localLabel = ontModel.getAnnotationProperty(METAG + "localLabel");
		AnnotationProperty annotationPropertyDataType = ontModel.getAnnotationProperty(METAG + "dataType");
		AnnotationProperty annotationPropertyUnit = ontModel.getAnnotationProperty(METAG + "unit");
		AnnotationProperty description = ontModel.getAnnotationProperty(METAG + "description");

		// 读取本体中的Language类，即MetaGraph中的语言文件夹
		OntClass language = ontModel.getOntClass(METAG + "Language");
		// 读取本体的对象属性，即元素之间的关系
		ObjectProperty languageIncludingProperty = ontModel.getObjectProperty(METAG + "languageIncludingProperty");
		// 遍历本体中的Language类下的子类，即建模语言
		for (ExtendedIterator<OntClass> it = language.listSubClasses(); it.hasNext();) {
			OntClass ontLanguage = it.next();
			// 遍历本体中的Language子类的等价类，即与子类有关系的类
			for (ExtendedIterator<OntClass> it2 = ontLanguage.listEquivalentClasses(); it2.hasNext();) {
				OntClass equivalentClass = it2.next();
				// 判断等价类是否具有属性languageIncludingProperty
				if (equivalentClass.asRestriction().getOnProperty().equals(languageIncludingProperty)) {
					// 将等价类具有的属性languageIncludingProperty的属性值取出来，即获取这个建模语言具有的属性元模型
					OntClass ontMetaProperty = (OntClass) equivalentClass.asRestriction().asSomeValuesFromRestriction()
							.getSomeValuesFrom();
					// 获取属性元模型的Id
					String metaPropertyName = ontMetaProperty.getLocalName();
					// 获取属性元模型的localLabel
					String metaPropertyLocalLabel = ontMetaProperty.getPropertyValue(localLabel).toString();
					// 获取属性元模型的注释属性description值(*)
					if (ontMetaProperty.getPropertyValue(description) != null) {
						String metaPropertyDescription = ontMetaProperty.getPropertyValue(description).toString();
					}
					// 获取属性元模型的数据类型dataType值(*)
					String metaPropertyDataType = ontMetaProperty.getPropertyValue(annotationPropertyDataType)
							.toString();
					// 获取属性元模型的注释属性unit值(*)
					String metaPropertyUnit;
					if (ontMetaProperty.hasProperty(annotationPropertyUnit)) {
						metaPropertyUnit = ontMetaProperty.getPropertyValue(annotationPropertyUnit).toString();
					}
				}
			}
		}
	}

	// 获取点元模型信息
	public static void getMetaPoint(String path) {
		// 命名空间（namespace）的URL，用于在本体建模中为实体、属性和其他元素提供唯一的标识符
		String METAG = "http://www.zkhoneycomb.com/formats/metagInOwl#";
		// 创建一个OntModel对象
		OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		// 从指定的文件路径(path)读取本体数据并将其加载到ontModel对象中。
		ontModel.read(path);
		// 读取本体的注释属性
		AnnotationProperty localLabel = ontModel.getAnnotationProperty(METAG + "localLabel");
		AnnotationProperty description = ontModel.getAnnotationProperty(METAG + "description");
		AnnotationProperty shape = ontModel.getAnnotationProperty(METAG + "shape");
		AnnotationProperty id = ontModel.getAnnotationProperty(METAG + "id");
		// 读取本体中的Language类，即MetaGraph中的语言文件夹
		OntClass language = ontModel.getOntClass(METAG + "Language");
		// 读取本体的对象属性，即元素之间的关系，语言包括的点元模型
		ObjectProperty languageIncludingPoint = ontModel.getObjectProperty(METAG + "languageIncludingPoint");
		// 遍历本体中的Language类下的子类，即建模语言
		for (ExtendedIterator<OntClass> it = language.listSubClasses(); it.hasNext();) {
			OntClass ontLanguage = it.next();
			String ontLanguageId = ontLanguage.getPropertyValue(id).toString();
			String ontLanguageName = ontLanguage.getPropertyValue(localLabel).toString();

			for (ExtendedIterator<OntClass> it2 = ontLanguage.listEquivalentClasses(); it2.hasNext();) {
				OntClass equivalentClass = it2.next();
				if (equivalentClass.asRestriction().getOnProperty().equals(languageIncludingPoint)) {
					OntClass ontMetaPoint = (OntClass) equivalentClass.asRestriction().asSomeValuesFromRestriction()
							.getSomeValuesFrom();
					// 获取点元模型的ID属性
					String metaPointName = ontMetaPoint.getLocalName();
					// 获取点元模型的localLabel属性
					String metaPointLocalLabel = ontMetaPoint.getPropertyValue(localLabel).toString();
					// 获取点元模型的description属性
					if (ontMetaPoint.getPropertyValue(description) != null) {
						String metaPointDescription = ontMetaPoint.getPropertyValue(description).toString();
					}
					// 获取点元模型的形状属性
					String metaPointShape = ontMetaPoint.getPropertyValue(shape).toString();

					// 点元模型具有的属性元模型
					for (Iterator point_it = ontMetaPoint.listEquivalentClasses(); point_it.hasNext();) {
						OntClass c2 = (OntClass) point_it.next();
						Restriction r2 = c2.asRestriction();

						if (r2.isSomeValuesFromRestriction()
								&& r2.getOnProperty().getLocalName().equals("hasProperty")) {
							SomeValuesFromRestriction sr2 = r2.asSomeValuesFromRestriction();
							OntClass metaPointProperty = (OntClass) sr2.getSomeValuesFrom();
						}

						if (r2.isAllValuesFromRestriction()
								&& r2.getOnProperty().getLocalName().equals("hasProperty")) {
							AllValuesFromRestriction sr2 = r2.asAllValuesFromRestriction();
							OntClass metaPointProperty = (OntClass) sr2.getAllValuesFrom();

						}
					}
				}
			}

		}
	}

	// 获取角色元模型信息
	public static void getMetaRole(String path) {
		// 命名空间（namespace）的URL，用于在本体建模中为实体、属性和其他元素提供唯一的标识符
		String METAG = "http://www.zkhoneycomb.com/formats/metagInOwl#";
		// 创建一个OntModel对象
		OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		// 从指定的文件路径(path)读取本体数据并将其加载到ontModel对象中。
		ontModel.read(path);
		// 读取本体的注释属性
		AnnotationProperty localLabel = ontModel.getAnnotationProperty(METAG + "localLabel");
		AnnotationProperty description = ontModel.getAnnotationProperty(METAG + "description");
		AnnotationProperty shape = ontModel.getAnnotationProperty(METAG + "shape");
		AnnotationProperty direction = ontModel.getAnnotationProperty(METAG + "direction");
		AnnotationProperty id = ontModel.getAnnotationProperty(METAG + "id");

		OntClass language = ontModel.getOntClass(METAG + "Language");
		ObjectProperty languageIncludingRole = ontModel.getObjectProperty(METAG + "languageIncludingRole");

		for (ExtendedIterator<OntClass> it = language.listSubClasses(); it.hasNext();) {
			OntClass ontLanguage = it.next();
			String ontLanguageId = ontLanguage.getPropertyValue(id).toString();
			String ontLanguageName = ontLanguage.getPropertyValue(localLabel).toString();

			for (ExtendedIterator<OntClass> it2 = ontLanguage.listEquivalentClasses(); it2.hasNext();) {
				OntClass equivalentClass = it2.next();
				if (equivalentClass.asRestriction().getOnProperty().equals(languageIncludingRole)) {
					OntClass ontMetaRole = (OntClass) equivalentClass.asRestriction().asSomeValuesFromRestriction()
							.getSomeValuesFrom();
					// 获取角色元模型的ID属性
					String metaRoleName = ontMetaRole.getLocalName();
					// 获取角色元模型的localLabel属性
					String metaRoleLocalLabel = ontMetaRole.getPropertyValue(localLabel).toString();
					// 获取角色元模型的description属性(*)
					if (ontMetaRole.getPropertyValue(description) != null) {
						String metaRoleDescription = ontMetaRole.getPropertyValue(description).toString();
					}
					// 获取角色元模型的direction属性
					String metaRoleDirection = ontMetaRole.getPropertyValue(direction).toString();
					// 获取角色元模型的shape属性
					String metaRoleShape = ontMetaRole.getPropertyValue(shape).toString();

					// 获取角色元模型具有的属性元模型
					for (Iterator role_it = ontMetaRole.listEquivalentClasses(); role_it.hasNext();) {
						OntClass c2 = (OntClass) role_it.next();
						Restriction r2 = c2.asRestriction();
						// 表示 r2 是一个 "某个值来自" 限制，并且应用于 "hasProperty" 属性。
						if (r2.isSomeValuesFromRestriction()
								&& r2.getOnProperty().getLocalName().equals("hasProperty")) {
							SomeValuesFromRestriction sr2 = r2.asSomeValuesFromRestriction();
							OntClass metaRoleProperty = (OntClass) sr2.getSomeValuesFrom();
						}
						// 表示 r2 是一个 "所有值来自" 限制，并且应用于 "hasProperty" 属性
						if (r2.isAllValuesFromRestriction()
								&& r2.getOnProperty().getLocalName().equals("hasProperty")) {
							AllValuesFromRestriction sr2 = r2.asAllValuesFromRestriction();
							OntClass metaRoleProperty = (OntClass) sr2.getAllValuesFrom();
						}
					}
				}
			}

		}
	}

	// 获取对象元模型信息
	public static void getMetaObject(String path) {
		// 命名空间（namespace）的URL，用于在本体建模中为实体、属性和其他元素提供唯一的标识符
		String METAG = "http://www.zkhoneycomb.com/formats/metagInOwl#";
		// 创建一个OntModel对象
		OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		// 从指定的文件路径(path)读取本体数据并将其加载到ontModel对象中。
		ontModel.read(path);
		// 读取本体的注释属性
		AnnotationProperty localLabel = ontModel.getAnnotationProperty(METAG + "localLabel");
		AnnotationProperty description = ontModel.getAnnotationProperty(METAG + "description");
		AnnotationProperty annotationPropertyScreenMode = ontModel.createAnnotationProperty(METAG + "screenMode");// visualizedMode
		AnnotationProperty annotationPropertyUnfoldDirection = ontModel
				.getAnnotationProperty(METAG + "UnfoldDirection");
		AnnotationProperty shape = ontModel.getAnnotationProperty(METAG + "shape");
		AnnotationProperty annotationPropertyIcon = ontModel.getAnnotationProperty(METAG + "icon");
		AnnotationProperty annotationPropertyImageAddress = ontModel.getAnnotationProperty(METAG + "imageAddress");
		AnnotationProperty id = ontModel.getAnnotationProperty(METAG + "id");

		OntClass language = ontModel.getOntClass(METAG + "Language");
		ObjectProperty languageIncludingObject = ontModel.getObjectProperty(METAG + "languageIncludingObject");

		for (ExtendedIterator<OntClass> it = language.listSubClasses(); it.hasNext();) {
			OntClass ontLanguage = it.next();
			String ontLanguageId = ontLanguage.getPropertyValue(id).toString();
			String ontLanguageName = ontLanguage.getPropertyValue(localLabel).toString();
			System.out.println(ontLanguageId + ", " + ontLanguageName);

			for (ExtendedIterator<OntClass> it2 = ontLanguage.listEquivalentClasses(); it2.hasNext();) {
				OntClass equivalentClass = it2.next();
				if (equivalentClass.asRestriction().getOnProperty().equals(languageIncludingObject)) {
					OntClass ontMetaObject = (OntClass) equivalentClass.asRestriction().asSomeValuesFromRestriction()
							.getSomeValuesFrom();
					// 获取对象元模型的ID属性
					String metaObjectName = ontMetaObject.getLocalName();
					// 获取对象元模型的localLabel属性
					String metaObjectLocalLabel = ontMetaObject.getPropertyValue(localLabel).toString();
					// 获取对象元模型的description属性
					if (ontMetaObject.getPropertyValue(description) != null) {
						String metaObjectDescription = ontMetaObject.getPropertyValue(description).toString();
					}
					// 获取对象元模型的形状属性
					String metaObjectShape = ontMetaObject.getPropertyValue(shape).toString();
					// 获取对象元模型的模式属性
					String metaObjectScreenMode = ontMetaObject.getPropertyValue(annotationPropertyScreenMode)
							.toString();// visualizedMode
					// 获取对象元模型的图片地址属性
					if (ontMetaObject.getPropertyValue(annotationPropertyImageAddress) != null) {
						String metaObjectImageAddress = ontMetaObject.getPropertyValue(annotationPropertyImageAddress)
								.toString();
					}
					// 获取对象元模型的图标属性
					if (ontMetaObject.getPropertyValue(annotationPropertyIcon) != null) {
						String metaObjectIcon = ontMetaObject.getPropertyValue(annotationPropertyIcon).toString();
					}
					// 获取对象元模型的展开方向属性
					if (ontMetaObject.getPropertyValue(annotationPropertyUnfoldDirection) != null) {
						String metaObjectUnfoldDirection = ontMetaObject
								.getPropertyValue(annotationPropertyUnfoldDirection).toString();
					}

					for (Iterator object_it = ontMetaObject.listEquivalentClasses(); object_it.hasNext();) {
						OntClass c2 = (OntClass) object_it.next();
						Restriction r2 = c2.asRestriction();
						// 获取对象元模型具有的属性元模型
						if (r2.isSomeValuesFromRestriction()
								&& r2.getOnProperty().getLocalName().equals("hasProperty")) {
							SomeValuesFromRestriction sr2 = r2.asSomeValuesFromRestriction();
							OntClass metaObjectProperty = (OntClass) sr2.getSomeValuesFrom();
							System.out.println(metaObjectProperty);
						}
						if (r2.isAllValuesFromRestriction()
								&& r2.getOnProperty().getLocalName().equals("hasProperty")) {
							AllValuesFromRestriction sr2 = r2.asAllValuesFromRestriction();
							OntClass metaObjectProperty = (OntClass) sr2.getAllValuesFrom();
						}
						// 获取对象元模型具有的点元模型
						if (r2.isSomeValuesFromRestriction()
								&& r2.getOnProperty().getLocalName().equals("linkObjectAndPoint")) {
							SomeValuesFromRestriction sr2 = r2.asSomeValuesFromRestriction();
							OntClass metaObjectIntersectionClass = (OntClass) sr2.getSomeValuesFrom();
							// IntersectionClass（交集类）是一种本体构造，用于表示两个或多个类的交集
							if (metaObjectIntersectionClass.isIntersectionClass()) {
								OntClass metaObjectPoint = null;
								for (Iterator and = metaObjectIntersectionClass.asIntersectionClass()
										.listOperands(); and.hasNext();) {
									OntClass and_class = (OntClass) and.next();
									if (!and_class.isRestriction()) {
										metaObjectPoint = and_class;
									} else {
										// 获取点的方向(数据属性)
										String pointDirection = and_class.asRestriction().asHasValueRestriction()
												.getHasValue().toString();
									}
								}

							}
						}

					}
				}
			}
		}
	}

	// 获取关系元模型信息
	public static void getMetaRelationship(String path) {
		// 命名空间（namespace）的URL，用于在本体建模中为实体、属性和其他元素提供唯一的标识符
		String METAG = "http://www.zkhoneycomb.com/formats/metagInOwl#";
		// 创建一个OntModel对象
		OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		// 从指定的文件路径(path)读取本体数据并将其加载到ontModel对象中。
		ontModel.read(path);
		// 读取本体的注释属性
		AnnotationProperty localLabel = ontModel.getAnnotationProperty(METAG + "localLabel");
		AnnotationProperty description = ontModel.getAnnotationProperty(METAG + "description");
		AnnotationProperty shape = ontModel.getAnnotationProperty(METAG + "shape");
		AnnotationProperty annotationPropertyIcon = ontModel.getAnnotationProperty(METAG + "icon");
		AnnotationProperty id = ontModel.getAnnotationProperty(METAG + "id");

		OntClass language = ontModel.getOntClass(METAG + "Language");

		ObjectProperty languageIncludingRelationship = ontModel
				.getObjectProperty(METAG + "languageIncludingRelationship");

		for (ExtendedIterator<OntClass> it = language.listSubClasses(); it.hasNext();) {
			OntClass ontLanguage = it.next();
			String ontLanguageId = ontLanguage.getPropertyValue(id).toString();
			String ontLanguageName = ontLanguage.getPropertyValue(localLabel).toString();

			for (ExtendedIterator<OntClass> it2 = ontLanguage.listEquivalentClasses(); it2.hasNext();) {
				OntClass equivalentClass = it2.next();
				if (equivalentClass.asRestriction().getOnProperty().equals(languageIncludingRelationship)) {
					OntClass ontMetaRelationship = (OntClass) equivalentClass.asRestriction()
							.asSomeValuesFromRestriction().getSomeValuesFrom();
					// 获取关系元模型的ID属性
					String metaRelationshipName = ontMetaRelationship.getLocalName();
					// 获取关系元模型的localLabel属性
					String metaRelationshipLocalLabel = ontMetaRelationship.getPropertyValue(localLabel).toString();
					// 获取关系元模型的Icon属性
					if (ontMetaRelationship.getPropertyValue(annotationPropertyIcon) != null) {
						String metaRelationshipIcon = ontMetaRelationship.getPropertyValue(annotationPropertyIcon)
								.toString();
					}
					// 获取关系元模型的description属性
					if (ontMetaRelationship.getPropertyValue(description) != null) {
						String metaRelationshipDescription = ontMetaRelationship.getPropertyValue(description)
								.toString();
					}
					// 获取关系元模型的形状属性
					String metaRelationshipShape = ontMetaRelationship.getPropertyValue(shape).toString();

					// 获取关系元模型具有的属性元模型和角色元模型
					for (Iterator relationship_it = ontMetaRelationship.listEquivalentClasses(); relationship_it
							.hasNext();) {
						OntClass c2 = (OntClass) relationship_it.next();
						Restriction r2 = c2.asRestriction();
						// 获取关系元模型具有的属性元模型
						if (r2.isSomeValuesFromRestriction()
								&& r2.getOnProperty().getLocalName().equals("hasProperty")) {
							SomeValuesFromRestriction sr2 = r2.asSomeValuesFromRestriction();
							OntClass metaRelationshipProperty = (OntClass) sr2.getSomeValuesFrom();
						}
						if (r2.isAllValuesFromRestriction()
								&& r2.getOnProperty().getLocalName().equals("hasProperty")) {
							AllValuesFromRestriction sr2 = r2.asAllValuesFromRestriction();
							OntClass metaRelationshipProperty = (OntClass) sr2.getAllValuesFrom();
						}
						// 获取关系元模型具有的角色元模型
						if (r2.isSomeValuesFromRestriction()
								&& r2.getOnProperty().getLocalName().equals("linkRelationshipAndRole")) {
							SomeValuesFromRestriction sr2 = r2.asSomeValuesFromRestriction();
							OntClass metaRelationshipRole = (OntClass) sr2.getSomeValuesFrom();
						}
					}
				}
			}

		}

	}

	// 获取图元模型信息
	public static void getMetaGraph(String path) {
		// 命名空间（namespace）的URL，用于在本体建模中为实体、属性和其他元素提供唯一的标识符
		String METAG = "http://www.zkhoneycomb.com/formats/metagInOwl#";
		// 创建一个OntModel对象
		OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		// 从指定的文件路径(path)读取本体数据并将其加载到ontModel对象中。
		ontModel.read(path);
		// 读取本体的注释属性
		AnnotationProperty localLabel = ontModel.getAnnotationProperty(METAG + "localLabel");
		AnnotationProperty description = ontModel.getAnnotationProperty(METAG + "description");
		AnnotationProperty annotationPropertyType = ontModel.getAnnotationProperty(METAG + "type");
		AnnotationProperty id = ontModel.getAnnotationProperty(METAG + "id");

		OntClass language = ontModel.getOntClass(METAG + "Language");
		ObjectProperty languageIncludingGraph = ontModel.getObjectProperty(METAG + "languageIncludingGraph");

		for (ExtendedIterator<OntClass> it = language.listSubClasses(); it.hasNext();) {
			OntClass ontLanguage = it.next();
			String ontLanguageId = ontLanguage.getPropertyValue(id).toString();
			String ontLanguageName = ontLanguage.getPropertyValue(localLabel).toString();

			for (ExtendedIterator<OntClass> it2 = ontLanguage.listEquivalentClasses(); it2.hasNext();) {
				OntClass equivalentClass = it2.next();
				if (equivalentClass.asRestriction().getOnProperty().equals(languageIncludingGraph)) {
					OntClass ontMetaGraph = (OntClass) equivalentClass.asRestriction().asSomeValuesFromRestriction()
							.getSomeValuesFrom();
					// 获取图元模型的ID属性
					String metaGraphName = ontMetaGraph.getLocalName();
					// 获取图元模型的localLabel属性
					String metaGraphLocalLabel = ontMetaGraph.getPropertyValue(localLabel).toString();
					// 获取图元模型的类型属性
					if (ontMetaGraph.hasProperty(annotationPropertyType)) {
						String metaGraphType = ontMetaGraph.getPropertyValue(annotationPropertyType).toString();
					}
					// 获取图元模型的描述属性
					if (ontMetaGraph.getPropertyValue(description) != null) {
						String metaGraphDescription = ontMetaGraph.getPropertyValue(description).toString();
					}

					OntClass metaGraphConnector = null;
					for (Iterator graph_it = ontMetaGraph.listEquivalentClasses(); graph_it.hasNext();) {
						OntClass c2 = (OntClass) graph_it.next();
						Restriction r2 = c2.asRestriction();
						// 获取图元模型具有的属性元模型
						if (r2.isSomeValuesFromRestriction()
								&& r2.getOnProperty().getLocalName().equals("hasProperty")) {
							SomeValuesFromRestriction sr2 = r2.asSomeValuesFromRestriction();
							OntClass metaGraphProperty = (OntClass) sr2.getSomeValuesFrom();
						}
						if (r2.isAllValuesFromRestriction()
								&& r2.getOnProperty().getLocalName().equals("hasProperty")) {
							AllValuesFromRestriction sr2 = r2.asAllValuesFromRestriction();
							OntClass metaGraphProperty = (OntClass) sr2.getAllValuesFrom();
						}
						// 获取图元模型具有的对象元模型
						if (r2.isSomeValuesFromRestriction()
								&& r2.getOnProperty().getLocalName().equals("graphIncludingObject")) {
							SomeValuesFromRestriction sr2 = r2.asSomeValuesFromRestriction();
							OntClass metaGraphObject = (OntClass) sr2.getSomeValuesFrom();
							System.out.println(metaGraphObject);
						}
						// 获取图元模型具有的关系元模型
						if (r2.isSomeValuesFromRestriction()
								&& r2.getOnProperty().getLocalName().equals("graphIncludingRelationship")) {
							SomeValuesFromRestriction sr2 = r2.asSomeValuesFromRestriction();
							OntClass metaGraphRelationship = (OntClass) sr2.getSomeValuesFrom();
							System.out.println(metaGraphRelationship);
						}
						// 获取图元模型具有的约束
						if (r2.isSomeValuesFromRestriction()
								&& r2.getOnProperty().getLocalName().equals("graphIncludingConnector")) {
							SomeValuesFromRestriction sr2 = r2.asSomeValuesFromRestriction();
							metaGraphConnector = (OntClass) sr2.getSomeValuesFrom();
							System.out.println(metaGraphConnector);
						}
					}

					// 图元模型Constraint
					if (metaGraphConnector != null) {
						for (Iterator connector_i = metaGraphConnector.listEquivalentClasses(); connector_i
								.hasNext();) {
							OntClass c3 = (OntClass) connector_i.next();
							// 获取图元模型的约束具体内容
							if (c3.isIntersectionClass()) {
								for (Iterator and_i = c3.asIntersectionClass().listOperands(); and_i.hasNext();) {
									OntClass and_one = (OntClass) and_i.next();
									if (and_one.isRestriction()) {
										OntClass c4 = (OntClass) and_one.asRestriction().asSomeValuesFromRestriction()
												.getSomeValuesFrom();
										// 获取图元模型中角色元模型的约束
										if (c4.getSuperClass().getLocalName().equals("Role")) {
											String connector_role = c4.getLocalName();
											System.out.println(connector_role);
										}
										// 获取图元模型中对象元模型的约束
										if (c4.getSuperClass().getLocalName().equals("Object")) {
											String connector_object = c4.getLocalName();
											System.out.println(connector_object);
										}
										// 获取图元模型中点元模型的约束
										if (c4.getSuperClass().hasSuperClass()
												&& c4.getSuperClass().getSuperClass().getLocalName().equals("Point")) {
											String connector_point = c4.getLocalName();
											System.out.println(connector_point);
										}
										// 获取图元模型中关系元模型的约束
										if (c4.getSuperClass().getLocalName().equals("Relationship")) {
											String connector_relationship = c4.getLocalName();
											System.out.println(connector_relationship);
										}

									}
								}
								System.out.println("one Connector over");
							}
							// 获取图元模型中具有分解剖视的对象元模型，以及对象元模型分解剖视的图元模型
							if (c3.isRestriction()) {
								Restriction c4 = c3.asRestriction();
								String DorE = null;// 记录是分解还是剖视(D or E)
								String DorE_object = null;// 记录分解剖视的对象
								String DorE_graph = null;// 记录分解剖视的图
								if (((OntClass) c4.asSomeValuesFromRestriction().getSomeValuesFrom())
										.isIntersectionClass()) {
									for (Iterator c4_i = ((OntClass) c4.asSomeValuesFromRestriction()
											.getSomeValuesFrom()).asIntersectionClass().listOperands(); c4_i
													.hasNext();) {
										OntClass c4_i_c = (OntClass) c4_i.next();
										if (c4_i_c.isRestriction()) {
											Restriction c4_i_r = c4_i_c.asRestriction();
											if (c4_i_r.isSomeValuesFromRestriction()) {
												DorE = c4_i_r.getOnProperty().getLocalName();
												// 获取对象元模型分解剖视的图元模型
												DorE_graph = c4_i_r.asSomeValuesFromRestriction().getSomeValuesFrom()
														.getLocalName();
											}

										} else {
											// 获取具有分解剖视的对象元模型
											DorE_object = c4_i_c.getLocalName();
										}
									}
								}
							}
						}

					}

				}
			}
		}
	}

	// 获取模型信息
	public static void getModel(String path) {
		// 命名空间（namespace）的URL，用于在本体建模中为实体、属性和其他元素提供唯一的标识符
		String METAG = "http://www.zkhoneycomb.com/formats/metagInOwl#";
		// 创建一个OntModel对象
		OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		// 从指定的文件路径(path)读取本体数据并将其加载到ontModel对象中。
		ontModel.read(path);
		// 读取本体的注释属性
		AnnotationProperty initialLocation = ontModel.getAnnotationProperty(METAG + "initialLocation");
		AnnotationProperty localLabel = ontModel.getAnnotationProperty(METAG + "localLabel");
		AnnotationProperty shape = ontModel.getAnnotationProperty(METAG + "shape");
		AnnotationProperty annotationPropertyColor = ontModel.createAnnotationProperty(METAG + "color");
		AnnotationProperty screenMode = ontModel.getAnnotationProperty(METAG + "screenMode");
		AnnotationProperty imageAddress = ontModel.getAnnotationProperty(METAG + "imageAddress");
		AnnotationProperty annotationPropertySdIdentification = ontModel
				.getAnnotationProperty(METAG + "SdIdentification");
		AnnotationProperty annotationPropertyText = ontModel.createAnnotationProperty(METAG + "text");
		AnnotationProperty annotationPropertyIconDisplay = ontModel.createAnnotationProperty(METAG + "iconDisplay");
		AnnotationProperty annotationPropertyFrameSize = ontModel.getAnnotationProperty(METAG + "frameSize");
		AnnotationProperty annotationPropertyLineType = ontModel.getAnnotationProperty(METAG + "linetype");

		AnnotationProperty lableDisplay = ontModel.getAnnotationProperty(METAG + "lableDisplay");

		AnnotationProperty annotationPropertyPolyLineLocation = ontModel
				.createAnnotationProperty(METAG + "polyLineLocation");
		AnnotationProperty startLocation = ontModel.createAnnotationProperty(METAG + "startLocation");
		AnnotationProperty endLocation = ontModel.createAnnotationProperty(METAG + "endLocation");
		AnnotationProperty annotationPropertyCifString = ontModel.createAnnotationProperty(METAG + "cifString");
		AnnotationProperty annotationPropertyStyle = ontModel.createAnnotationProperty(METAG + "style");
		AnnotationProperty annotationPropertyInitial = ontModel.createAnnotationProperty(METAG + "initial");
		AnnotationProperty annotationPropertyType = ontModel.createAnnotationProperty(METAG + "type");
		AnnotationProperty annotationPropertyModelSize = ontModel.createAnnotationProperty(METAG + "modelSize");
		AnnotationProperty annotationPropertySmooth = ontModel.createAnnotationProperty(METAG + "smooth");
		AnnotationProperty annotationPropertyJumpStatus = ontModel.createAnnotationProperty(METAG + "jumpStatus");
		AnnotationProperty annotationPropertyJumpType = ontModel.createAnnotationProperty(METAG + "jumpType");
		AnnotationProperty annotationPropertyJumpReverse = ontModel.createAnnotationProperty(METAG + "jumpReverse");
		AnnotationProperty annotationPropertyAvoidObstructions = ontModel
				.createAnnotationProperty(METAG + "avoidObstructions");
		AnnotationProperty annotationPropertyClosestDistance = ontModel
				.createAnnotationProperty(METAG + "closestDistance");
		AnnotationProperty annotationPropertyQueue = ontModel.createAnnotationProperty(METAG + "queue");
		AnnotationProperty annotationPropertyRoutingType = ontModel.getAnnotationProperty(METAG + "routingType");
		AnnotationProperty annotationPropertyStrikethrough = ontModel.getAnnotationProperty(METAG + "strikeThrough");
		AnnotationProperty annotationPropertyUnderline = ontModel.getAnnotationProperty(METAG + "underline");

		ObjectProperty decomposeProp = ontModel.createObjectProperty(METAG + "decompose");
		ObjectProperty explodeProp = ontModel.createObjectProperty(METAG + "explode");
		ObjectProperty hasPropertyProp = ontModel.createObjectProperty(METAG + "hasProperty");

		OntClass language = ontModel.getOntClass(METAG + "Language");

		ObjectProperty languageIncludingGraph = ontModel.getObjectProperty(METAG + "languageIncludingGraph");
		for (ExtendedIterator<OntClass> it = language.listSubClasses(); it.hasNext();) {
			OntClass ontLanguage = it.next();
			String ontLanguageName = ontLanguage.getLocalName();

			for (ExtendedIterator<OntClass> it2 = ontLanguage.listEquivalentClasses(); it2.hasNext();) {
				OntClass equivalentClass = it2.next();
				if (equivalentClass.asRestriction().getOnProperty().equals(languageIncludingGraph)) {
					OntClass ontMetaGraph = (OntClass) equivalentClass.asRestriction().asSomeValuesFromRestriction()
							.getSomeValuesFrom();
					for (Iterator il2 = ontMetaGraph.listInstances(); il2.hasNext();) {
						Individual karmaModel = (Individual) il2.next();
						// 获取模型Id属性
						String karmaModelName = karmaModel.getLocalName();
						// 获取模型localLabel属性
						String karmaModelLocalLabel = karmaModel.getPropertyValue(localLabel).toString();
						// 获取模型Cif属性
						if (karmaModel.hasProperty(annotationPropertyCifString)) {
							String karmaModelCif = karmaModel.getPropertyValue(annotationPropertyCifString).toString();
						}

						for (Iterator ig = karmaModel.listProperties(); ig.hasNext();) {
							Statement igs = (Statement) ig.next();// Statement 是 Apache Jena 中的类，用于表示 RDF
																	// 三元组中的语句，包括主语、谓词和宾语。
							// 1.获取模型的属性及属性值
							if (igs.getPredicate().getLocalName().equals("hasProperty")) {// igs.getPredicate()获取该属性的谓词
								// 获取模型的属性实例
								Individual graphProperty = ontModel.getIndividual(igs.getResource().getURI());
								// 获取模型的属性值
								String graphPropertyValue = "";
								for (Iterator igp = graphProperty.listProperties(); igp.hasNext();) {
									Statement igps = (Statement) igp.next();
									if (igps.getPredicate().getLocalName().equals("value")) {
										graphPropertyValue = igps.getString();
									}
								}
							}
							// 2.获取模型的对象实例
							if (igs.getPredicate().getLocalName().equals("graphIncludingObject")) {
								// 获取模型的对象实例
								Individual graph_Object_Individual = ontModel.getIndividual(igs.getResource().getURI());
								String graph_Object_IndividualName = graph_Object_Individual.getLocalName();
								String graph_Object_IndividualLocalLabel = graph_Object_Individual
										.getPropertyValue(localLabel).toString();
								String graph_Object_IndividualShape = graph_Object_Individual.getPropertyValue(shape)
										.toString();
								// 获取模型的对象实例具有的注释属性
								if (graph_Object_Individual.hasProperty(annotationPropertyColor)) {
									String graph_Object_IndividualColor = graph_Object_Individual
											.getPropertyValue(annotationPropertyColor).toString();
								}
								String[] locations = graph_Object_Individual.getPropertyValue(initialLocation)
										.toString().split(",");

								if (graph_Object_Individual.hasProperty(annotationPropertyCifString)) {
									String graph_Object_IndividualCifString = graph_Object_Individual
											.getPropertyValue(annotationPropertyCifString).toString()
											.replace("\\\"", "\"");
								}
								if (graph_Object_Individual.hasProperty(annotationPropertyStyle)) {
									String graph_Object_IndividualStyle = graph_Object_Individual
											.getPropertyValue(annotationPropertyStyle).toString();
								}
								if (graph_Object_Individual.hasProperty(annotationPropertyInitial)) {
									String graph_Object_IndividualInitial = graph_Object_Individual
											.getPropertyValue(annotationPropertyInitial).toString();
								}
								if (graph_Object_Individual.hasProperty(annotationPropertyType)) {
									String graph_Object_IndividualType = graph_Object_Individual
											.getPropertyValue(annotationPropertyType).toString();
								}
								if (graph_Object_Individual.hasProperty(imageAddress)) {
									String graph_Object_IndividualImageAddress = graph_Object_Individual
											.getPropertyValue(imageAddress).toString();
								}
								if (graph_Object_Individual.hasProperty(annotationPropertyIconDisplay)) {
									String graph_Object_IndividualIconDisplay = graph_Object_Individual
											.getPropertyValue(annotationPropertyIconDisplay).toString();
								}
								if (graph_Object_Individual.hasProperty(annotationPropertyText)) {
									String[] texts = graph_Object_Individual.getPropertyValue(annotationPropertyText)
											.toString().split(",");
								}
								if (graph_Object_Individual.hasProperty(annotationPropertyLineType)) {
									String graph_Object_IndividualLineType = graph_Object_Individual
											.getPropertyValue(annotationPropertyLineType).toString();
								}
								if (graph_Object_Individual.hasProperty(annotationPropertyFrameSize)) {
									Integer graph_Object_IndividualFrameSize = Integer.parseInt(graph_Object_Individual
											.getPropertyValue(annotationPropertyFrameSize).toString());
								}
								if (graph_Object_Individual.hasProperty(annotationPropertySdIdentification)) {
									String graph_Object_IndividualSdIdentification = graph_Object_Individual
											.getPropertyValue(annotationPropertySdIdentification).toString();
								}
								// 2.1 获取模型的对象实例具有的属性实例及属性值
								for (Iterator igp = graph_Object_Individual.listProperties(); igp.hasNext();) {
									Statement igps = (Statement) igp.next();
									if (igps.getPredicate().getLocalName().equals("hasProperty")) {
										// 获取模型的对象实例具有的属性实例
										Individual object_Property_Individual = ontModel
												.getIndividual(igps.getResource().getURI());
										// 找到属性值
										String object_Property_Individual_Value = "";
										for (Iterator igp2 = object_Property_Individual.listProperties(); igp2
												.hasNext();) {
											Statement igps2 = (Statement) igp2.next();
											if (igps2.getPredicate().getLocalName().equals("value")) {
												object_Property_Individual_Value = igps2.getString();
											}
										}

										if (object_Property_Individual.hasProperty(annotationPropertyText)) {
											String[] texts = object_Property_Individual
													.getPropertyValue(annotationPropertyText).toString().split(",");
										}

										if (object_Property_Individual.hasProperty(annotationPropertyColor)) {
											String object_Property_Individual_Color = object_Property_Individual
													.getPropertyValue(annotationPropertyColor).toString();
										}
										if (object_Property_Individual
												.getPropertyValue(annotationPropertyStrikethrough) != null) {
											String object_Property_Individual_Strikethrough = object_Property_Individual
													.getPropertyValue(annotationPropertyStrikethrough).toString();
										}
										if (object_Property_Individual
												.getPropertyValue(annotationPropertyUnderline) != null) {
											String object_Property_Individual_Underline = object_Property_Individual
													.getPropertyValue(annotationPropertyUnderline).toString();
										}

										for (Iterator igp2 = object_Property_Individual.listProperties(); igp2
												.hasNext();) {
											Statement igps2 = (Statement) igp2.next();
											if (igps2.getPredicate().getLocalName().equals("cloneProperty")) {
												Individual object_property_clone_Individual = ontModel
														.getIndividual(igps2.getResource().getURI());
												String object_property_clone = object_property_clone_Individual
														.getLocalName();
											}
										}

									}
									// 2.2 获取模型的对象实例剖视、分解、克隆模型、克隆对象的实例
									if (igps.getPredicate().getLocalName().equals("explode")) {
										Individual explodeIndi = ontModel.getIndividual(igps.getResource().getURI());
									}
									if (igps.getPredicate().getLocalName().equals("decompose")) {
										Individual decomposeIndi = ontModel.getIndividual(igps.getResource().getURI());
									}
									if (igps.getPredicate().getLocalName().equals("cloneModel")) {
										Individual cloneModelIndi = ontModel.getIndividual(igps.getResource().getURI());
									}
									if (igps.getPredicate().getLocalName().equals("cloneObject")) {
										Individual clonObjectIndi = ontModel.getIndividual(igps.getResource().getURI());
									}
									// 2.3 获取模型的对象实例具有的点实例
									if (igps.getPredicate().getLocalName().equals("linkObjectAndPoint")) {
										Individual point_Individual = ontModel
												.getIndividual(igps.getResource().getURI());
										String point_Individual_Id = point_Individual.getLocalName();
										String point_Individual_LocalLabel = point_Individual
												.getPropertyValue(localLabel).toString();
										String point_Individual_Shape = point_Individual.getPropertyValue(shape)
												.toString();
										String point_Individual_Color = point_Individual
												.getPropertyValue(annotationPropertyColor).toString();
										String[] locations2 = point_Individual.getPropertyValue(initialLocation)
												.toString().split(",");
										String point_Individual_LableDisplay = point_Individual
												.getPropertyValue(lableDisplay).toString();
										if (point_Individual.hasProperty(annotationPropertyText)) {
											String[] texts = point_Individual.getPropertyValue(annotationPropertyText)
													.toString().split(",");
										}

										// 2.3.1 获取点实例的属性实例
										for (Iterator igp2 = point_Individual.listProperties(); igp2.hasNext();) {
											Statement igps2 = (Statement) igp2.next();
											if (igps2.getPredicate().getLocalName().equals("hasProperty")) {
												// 找到点实例属性
												Individual point_Property_Individual = ontModel
														.getIndividual(igps2.getResource().getURI());
												for (Iterator igp3 = point_Property_Individual.listProperties(); igp3
														.hasNext();) {
													Statement igps3 = (Statement) igp3.next();
													if (igps3.getPredicate().getLocalName().equals("value")) {
														String point_Property_Individual_Value = igps3.getString();
													}
												}
											}
											if (igps2.getPredicate().getLocalName().equals("clonePoint")) {
												Individual clonePointIndi = ontModel
														.getIndividual(igps2.getResource().getURI());
											}

										}
									}

								}
							}
							// 3. 获取模型的关系实例及对应的信息
							if (igs.getPredicate().getLocalName().equals("graphIncludingRelationship")) {
								// 获取关系实例
								Individual graph_Rela_Individual = ontModel.getIndividual(igs.getResource().getURI());

								// 3.1获取关系实例两端的角色实例及相关信息
								for (Iterator igp = graph_Rela_Individual.listProperties(); igp.hasNext();) {
									Statement igps = (Statement) igp.next();
									// 3.1.1 获取关系实例两端的角色实例
									if (igps.getPredicate().getLocalName().equals("linkRelationshipAndRole")) {
										Individual role_Individual = ontModel
												.getIndividual(igps.getResource().getURI());
										String role_Individual_Id = role_Individual.getLocalName();
										String role_Individual_LocalLabel = role_Individual.getPropertyValue(localLabel)
												.toString();
										String role_Individual_Shape = role_Individual.getPropertyValue(shape)
												.toString();

										// 3.1.2 获取关系实例两端的角色实例的属性实例
										for (Iterator igp2 = role_Individual.listProperties(); igp2.hasNext();) {
											Statement igps2 = (Statement) igp2.next();
											if (igps2.getPredicate().getLocalName().equals("hasProperty")) {
												Individual role_Property_Individual = ontModel
														.getIndividual(igps2.getResource().getURI());
												for (Iterator igp3 = role_Property_Individual.listProperties(); igp3
														.hasNext();) {
													Statement igps3 = (Statement) igp3.next();
													if (igps3.getPredicate().getLocalName().equals("value")) {
														String role_Property_Individual_Value = igps3.getString();
													}
												}
											}
										}
									}
								}
								;
								// 关系实例的注释属性
								String role_Property_Individual_Id = graph_Rela_Individual.getLocalName();
								String role_Property_Individual_LocalLabel = graph_Rela_Individual
										.getPropertyValue(localLabel).toString();
								if (graph_Rela_Individual.hasProperty(annotationPropertyPolyLineLocation)) {
									String role_Property_Individual_PolyLineLocation = graph_Rela_Individual
											.getPropertyValue(annotationPropertyPolyLineLocation).toString();
								}
								String role_Property_Individual_LableDisplay = graph_Rela_Individual
										.getPropertyValue(lableDisplay).toString();
								String role_Property_Individual_Shape = graph_Rela_Individual.getPropertyValue(shape)
										.toString();
								String[] starts = graph_Rela_Individual.getPropertyValue(startLocation).toString()
										.split(",");
								String[] ends = graph_Rela_Individual.getPropertyValue(endLocation).toString()
										.split(",");

								if (graph_Rela_Individual.hasProperty(annotationPropertyCifString)) {
									String role_Property_Individual_CifString = graph_Rela_Individual
											.getPropertyValue(annotationPropertyCifString).toString()
											.replace("\\\"", "\"");
								}
								if (graph_Rela_Individual.getPropertyValue(annotationPropertyAvoidObstructions)
										.toString().equals("true")) {

								}
								if (graph_Rela_Individual.getPropertyValue(annotationPropertyClosestDistance).toString()
										.equals("true")) {
								}
								if (graph_Rela_Individual.hasProperty(annotationPropertyJumpStatus)) {
									String role_Property_Individual_JumpStatus = graph_Rela_Individual
											.getPropertyValue(annotationPropertyJumpStatus).toString();
								}
								if (graph_Rela_Individual.hasProperty(annotationPropertyJumpType)) {
									String role_Property_Individual_JumpType = graph_Rela_Individual
											.getPropertyValue(annotationPropertyJumpType).toString();
								}
								if (graph_Rela_Individual.getPropertyValue(annotationPropertyJumpReverse).toString()
										.equals("true")) {

								}
								if (graph_Rela_Individual.hasProperty(annotationPropertyRoutingType)) {
									String role_Property_Individual_RoutingType = graph_Rela_Individual
											.getPropertyValue(annotationPropertyRoutingType).toString();
								}
								if (graph_Rela_Individual.hasProperty(annotationPropertySmooth)) {
									String role_Property_Individual_Smooth = graph_Rela_Individual
											.getPropertyValue(annotationPropertySmooth).toString();
								}
								if (graph_Rela_Individual.hasProperty(annotationPropertyText)) {
									String[] texts = graph_Rela_Individual.getPropertyValue(annotationPropertyText)
											.toString().split(",");
								}
								if (graph_Rela_Individual.hasProperty(annotationPropertySdIdentification)) {
									String role_Property_Individual_SdIdentification = graph_Rela_Individual
											.getPropertyValue(annotationPropertySdIdentification).toString();
								}
								if (graph_Rela_Individual.hasProperty(annotationPropertyColor)) {
									String role_Property_Individual_Color = graph_Rela_Individual
											.getPropertyValue(annotationPropertyColor).toString();
								}
								Integer role_Property_Individual_ModelSize = Integer.parseInt(
										graph_Rela_Individual.getPropertyValue(annotationPropertyModelSize).toString());

								// 3.2 获取关系实例的属性实例及相关信息
								for (Iterator igp = graph_Rela_Individual.listProperties(); igp.hasNext();) {
									Statement igps = (Statement) igp.next();
									if (igps.getPredicate().getLocalName().equals("hasProperty")) {
										// 获取关系实例的属性实例
										Individual rela_Property_Individual = ontModel
												.getIndividual(igps.getResource().getURI());
										// 找到属性值
										for (Iterator igp2 = rela_Property_Individual.listProperties(); igp2
												.hasNext();) {
											Statement igps2 = (Statement) igp2.next();
											if (igps2.getPredicate().getLocalName().equals("value")) {
												String rela_Property_Individual_value = igps2.getString();
											}
										}
									}
								}
							}
							// 4. 获取模型的约束实例及对应的信息(GOPPRR-E的E)
							if (igs.getPredicate().getLocalName().equals("graphIncludingConnector")) {
								// 获取模型的约束实例
								Individual connetor_Individual = ontModel.getIndividual(igs.getResource().getURI());
								String connectPoint = null;
								String connectObject = null;
								String connectRelationship = null;
								String connectRole = null;
								for (Iterator igp = connetor_Individual.listProperties(); igp.hasNext();) {
									Statement igps = (Statement) igp.next();
									if (igps.getPredicate().getLocalName().equals("linkFromRelationship")) {
										connectRelationship = igps.getResource().getLocalName();
										System.out.println(connectRelationship);
									} else if (igps.getPredicate().getLocalName().equals("linkRelationshipAndRole")) {
										connectRole = igps.getResource().getLocalName();
										System.out.println(connectRole);
									} else if (igps.getPredicate().getLocalName().equals("linkToObject")) {
										connectObject = igps.getResource().getLocalName();
										System.out.println(connectObject);
									} else if (igps.getPredicate().getLocalName().equals("linkObjectAndPoint")) {
										connectPoint = igps.getResource().getLocalName();
										System.out.println(connectPoint);
									}
								}
								if (connectPoint == null) {
									// 约束方式
//										connectRelationship + "." + connectRole, connectObject;
									System.out.println(connectObject + "," + connectRelationship + "." + connectRole);
								} else {
									// 约束方式
//										connectRelationship + "." + connectRole,connectObject + "." + connectPoint
									System.out.println(connectRelationship + "." + connectRole + "," + connectObject
											+ "." + connectPoint);
								}
							}
						}
					}
				}
			}
		}
	}

	public static void main(String[] args) {
		String path = "D:\\208培训\\MetaGraph\\workspace\\Electric_Vehicle\\Electric_Vehicle_BFO.owl";
		// 获取语言信息
		getLanguage(path);
		// 获取属性元模型信息
		getMetaProperty(path);
		// 获取点元模型信息
		getMetaPoint(path);
		// 获取角色元模型信息
		getMetaRole(path);
		// 获取对象元模型信息
		getMetaObject(path);
		// 获取关系元模型信息
		getMetaRelationship(path);
		// 获取图元模型信息
		getMetaGraph(path);
		// 获取模型信息
		getModel(path);
	}
}
