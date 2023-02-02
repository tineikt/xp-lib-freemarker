/**
 *  Use `resolve(..)` to get the ResourceKey of a Freemarker file in your project
 * @typedef {Object} ResourceKey
 * @property {string} application The key of the xp application
 * @property {string} path The path within the xp application
 */
/**
 * Pass in a Freemarker-template as a string
 * @typedef {Object} TextTemplate
 * @property {string} template A string containing the Freemarker code
 * @property {string} [baseDirPath] An optional base path to use (on your local machine).
 *   The default value is the root inside your deployed xp application
 */
/**
 * Specify a local file on your machine to use as the template.
 * This is useful for local testing without re-deploying the application.
 * You can just change the file and refresh the page.
 * @typedef {Object} File
 * @property {string} filePath The path to a file on your local machine
 * @property {string} [baseDirPath] An optional base path to use (on your local machine).
 *    The default value is the root inside your deployed xp application
 */

/**
 * Freemarker template related functions.
 *
 * @example
 * var freemarkerLib = require('/lib/tineikt/freemarker');
 *
 * @module lib/xp/freemarker
 */

var service = __.newBean('no.tine.xp.lib.freemarker.FreemarkerService');

/**
 * This function renders a view using Freemarker.
 *
 * @param {ResourceKey | TextTemplate | File} view Enonic Resource key, inline template or local file path
 * @param {object} model Model that is passed to the view.
 * @returns {string} The rendered output.
 */
exports.render = function (view, model) {
    var processor = service.newProcessor();

    // If using an inline template or file the base directory path can be specified
    if (view.baseDirPath) {
        processor.baseDirPath = view.baseDirPath;
    }

    // configure processor with correct template type
    if (view.template) {
        processor.textTemplate = view.template;
    } else if(view.filePath) {
        processor.filePath = view.filePath;
    } else {
        processor.view = view;
    }

    processor.model = __.toScriptValue(model);

    return processor.process();
};
