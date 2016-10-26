/**
 * Freemarker template related functions.
 *
 * @example
 * var freemarkerLib = require('/lib/xp/freemarker');
 *
 * @module lib/xp/freemarker
 */

var service = __.newBean('no.tine.xp.lib.freemarker.FreemarkerService');

/**
 * This function renders a view using Freemarker.
 *
 * @param view Location of the view. Use `resolve(..)` to resolve a view.
 * @param {object} model Model that is passed to the view.
 * @returns {string} The rendered output.
 */
exports.render = function (view, model) {
    var processor = service.newProcessor();
    processor.view = view;
    processor.model = __.toScriptValue(model);
    return processor.process();
};