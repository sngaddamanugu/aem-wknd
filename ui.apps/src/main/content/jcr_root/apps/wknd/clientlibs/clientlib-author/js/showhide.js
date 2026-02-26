(function (document, $) {
    "use strict";

    $(document).on("foundation-contentloaded", function (e) {
        checkboxShowHideHandler($(".cq-dialog-checkbox-showhide", e.target));
    });

    $(document).on("change", ".cq-dialog-checkbox-showhide", function (e) {
        checkboxShowHideHandler($(this));
    });

    function checkboxShowHideHandler(el) {
        el.each(function (i, element) {
            if($(element).is("coral-checkbox")) {
                Coral.commons.ready(element, function (component) {
                    showHide(component, element);
                    component.on("change", function () {
                        showHide(component, element);
                    });
                });
            } else {
                let component = $(element).data("checkbox");
                if (component) {
                    showHide(component, element);
                }
            }
        })
    }

    function showHide(component, element) {
        console.log('showing');
        let target = $(element).data("cqDialogCheckboxShowhideTarget");
        let $target = $(target);


        if (target) {
            $target.addClass("hide");
            if (component.checked) {
                $target.removeClass("hide");
            }
        }
    }
})(document, Granite.$);
