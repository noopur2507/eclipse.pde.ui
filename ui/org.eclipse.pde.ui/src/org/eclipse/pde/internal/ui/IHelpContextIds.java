package org.eclipse.pde.internal.ui;

/**
 *
 */
public interface IHelpContextIds {
	public static final String PREFIX = "org.eclipse.pde.doc.user" + ".";
	
	// Preference pages
	public static final String MAIN_PREFERENCE_PAGE = PREFIX + "main_preference_page";
    public static final String BUILDPATH_PREFERENCE_PAGE = PREFIX + "buildpath_preference_page";
    public static final String EDITOR_PREFERENCE_PAGE = PREFIX + "editor_preference_page";
	public static final String SOURCE_PREFERENCE_PAGE = PREFIX + "source_preference_page";
	public static final String COMPILERS_PREFERENCE_PAGE = PREFIX + "compilers_preference_page";
	public static final String TARGET_ENVIRONMENT_PREFERENCE_PAGE = PREFIX + "target_environment_preference_page";
 	public static final String TARGET_PLATFORM_PREFERENCE_PAGE = PREFIX + "target_platform_preference_page";
 	
 	// Views
 	public static final String PLUGINS_VIEW = PREFIX + "plugins_view";	
 	public static final String DEPENDENCIES_VIEW = PREFIX + "dependencies_view";
 	
 	// Wizard Pages
 	public static final String NEW_PROJECT_STRUCTURE_PAGE = PREFIX + "new_project_structure_page";
 	public static final String NEW_FRAGMENT_STRUCTURE_PAGE = PREFIX + "new_fragment_structure_page";
 	public static final String NEW_PROJECT_CODE_GEN_PAGE = PREFIX + "new_project_code_gen_page";
 	public static final String NEW_FRAGMENT_CODE_GEN_PAGE = PREFIX + "new_fragment_code_gen_page";
 	public static final String NEW_PROJECT_REQUIRED_DATA = PREFIX + "new_project_required_data";
 	public static final String NEW_FRAGMENT_REQUIRED_DATA = PREFIX + "new_fragment_required_data";
 	
 	public static final String NEW_FEATURE_DATA = PREFIX + "new_feature_data";
 	public static final String NEW_FEATURE_REFERENCED_PLUGINS = PREFIX + "new_feature_referenced_plugins";
 	
 	public static final String CONVERTED_PROJECTS = PREFIX + "converted_projects";
 	public static final String NEW_SCHEMA = PREFIX + "new_schema";
 	
 	public static final String PLUGIN_IMPORT_FIRST_PAGE = PREFIX + "plugin_import_first_page";
 	public static final String PLUGIN_IMPORT_SECOND_PAGE = PREFIX + "plugin_import_second_page";
 	
	public static final String FEATURE_IMPORT_FIRST_PAGE = PREFIX + "feature_import_first_page";
	public static final String FEATURE_IMPORT_SECOND_PAGE = PREFIX + "feature_import_second_page";
	
	public static final String PLUGIN_EXPORT_WIZARD = PREFIX + "plugin_export_wizard";
	public static final String FEATURE_EXPORT_WIZARD = PREFIX + "feature_export_wizard";	
 	
 	public static final String TEMPLATE_SELECTION = PREFIX + "template_selection";
 	public static final String TEMPLATE_EDITOR = PREFIX + "template_editor";
 	public static final String TEMPLATE_HELLO_WORLD = PREFIX + "template_hello_world";
 	public static final String TEMPLATE_HELP = PREFIX + "template_help";
 	public static final String TEMPLATE_MULTIPAGE_EDITOR = PREFIX + "template_multipage_editor";
 	public static final String TEMPLATE_NEW_WIZARD = PREFIX + "template_new_wizard";
 	public static final String TEMPLATE_PERSPECTIVE_EXTENSIONS = PREFIX + "template_perspective_extensions";
 	public static final String TEMPLATE_POPUP_MENU = PREFIX + "template_popup_menu";
 	public static final String TEMPLATE_PREFERENCE_PAGE = PREFIX + "template_preference_page";
 	public static final String TEMPLATE_PROPERTY_PAGE = PREFIX + "template_property_page";
 	public static final String TEMPLATE_VIEW = PREFIX + "template_view";
 	
 	public static final String MANIFEST_ADD_DEPENDENCIES = PREFIX + "manifest_add_dependencies";
 	public static final String ADD_EXTENSIONS_MAIN = PREFIX + "add_extensions_main";
 	public static final String ADD_EXTENSIONS_SCHEMA_BASED = PREFIX + "add_extensions_schema_based";
 	public static final String JAVA_ATTRIBUTE_WIZARD_PAGE = PREFIX + "java_attribute_wizard_page";
	public static final String UPDATE_CLASSPATH = PREFIX + "update_classpath";
	public static final String FEATURE_ADD_REQUIRED_WIZARD = PREFIX + "feature_add_required_wizard";
	public static final String FEATURE_ADD_PACKAGED_WIZARD = PREFIX + "feature_add_packaged_wizard";
	public static final String FEATURE_INCLUDED_FEATURES_WIZARD = PREFIX + "feature_included_features_wizard";
	public static final String FEATURE_PORTABILITY_WIZARD = PREFIX + "feature_portability_wizard";

	public static final String PLUGINS_CONTAINER_PAGE = PREFIX + "plugins_container_page";
	
	// dialogs
	public static final String FRAGMENT_ADD_TARGET = PREFIX + "fragment_add_target";
	public static final String BUILD_ADD_VARIABLE = PREFIX + "build_add_variable";
	public static final String SCHEMA_TYPE_RESTRICTION = PREFIX + "schema_type_restriction";
	public static final String SEARCH_PAGE = PREFIX + "search_page";
	public static final String LAUNCHER_BASIC = PREFIX + "launcher_basic";
	public static final String LAUNCHER_ADVANCED = PREFIX + "launcher_advanced";
	public static final String LAUNCHER_TRACING = PREFIX + "launcher_tracing";
	public static final String SOURCE_ATTACHMENT = PREFIX + "source_attachment";
	
	// Generic source page
	public static final String MANIFEST_SOURCE_PAGE = PREFIX + "manifest_source_page";
	
 	// Manifest Editor
 	public static final String MANIFEST_WELCOME = PREFIX + "manifest_welcome";
 	public static final String MANIFEST_PLUGIN_OVERVIEW = PREFIX + "manifest_plugin_overview";
 	public static final String MANIFEST_PLUGIN_DEPENDENCIES = PREFIX + "manifest_plugin_dependencies";
 	public static final String MANIFEST_PLUGIN_RUNTIME = PREFIX + "manifest_plugin_runtime";
 	public static final String MANIFEST_PLUGIN_EXTENSIONS = PREFIX + "manifest_plugin_extensions";
 	public static final String MANIFEST_PLUGIN_EXT_POINTS = PREFIX + "manifest_plugin_ext_points";
 	
 	public static final String MANIFEST_FRAGMENT_OVERVIEW = PREFIX + "manifest_fragment_overview";
 	public static final String MANIFEST_FRAGMENT_DEPENDENCIES = PREFIX + "manifest_fragment_dependencies";
 	public static final String MANIFEST_FRAGMENT_RUNTIME = PREFIX + "manifest_fragment_runtime";
 	public static final String MANIFEST_FRAGMENT_EXTENSIONS = PREFIX + "manifest_fragment_extensions";
 	public static final String MANIFEST_FRAGMENT_EXT_POINTS = PREFIX + "manifest_fragment_ext_points";
 
 	// Build Properties Editor
 	public static final String BUILD_PAGE = PREFIX + "build_page";
 	
 	// Schema Editor
 	public static final String SCHEMA_EDITOR_MAIN = PREFIX + "schema_editor_main";
 	public static final String SCHEMA_EDITOR_DOC = PREFIX + "schema_editor_doc";
 	
 	// Feature Editor
 	public static final String MANIFEST_FEATURE_OVERVIEW = PREFIX + "manifest_feature_overview";
 	public static final String MANIFEST_FEATURE_INFO = PREFIX + "manifest_feature_info";
 	public static final String MANIFEST_FEATURE_CONTENT = PREFIX + "manifest_feature_content";
 	public static final String MANIFEST_FEATURE_ADVANCED = PREFIX + "manifest_feature_advanced";
 	
 	public static final String FEATURE_SYNCHRONIZE_VERSIONS = PREFIX + "feature_synchronize_versions";

	// Site Editor
	public static final String MANIFEST_SITE_OVERVIEW = PREFIX + "manifest_site_overview";
	public static final String MANIFEST_SITE_BUILD = PREFIX + "manifest_sute_build";
	public static final String MANIFEST_SITE_FEATURES = PREFIX + "manifest_site_features";
	public static final String MANIFEST_SITE_ARCHIVES = PREFIX + "manifest_site_archives";	
	public static final String NEW_CATEGORY_DEF_DIALOG = PREFIX + "new_category_def_dialog";	
	
}
