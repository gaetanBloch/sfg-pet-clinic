package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.services.OwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Set;

@RequiredArgsConstructor
@RequestMapping(OwnerController.URL_OWNERS)
@Controller
final class OwnerController {
    static final String VIEW_OWNER_FIND_FORM = "owners/findOwners";
    static final String VIEW_OWNERS_LIST = "owners/ownersList";
    static final String VIEW_OWNER_DETAILS = "owners/ownerDetails";
    static final String VIEW_OWNER_CREATE_OR_UPDATE_FORM = "owners/createOrUpdateOwnerForm";
    static final String URL_OWNERS = "/owners";
    static final String URL_FIND = "/find";
    static final String URL_NEW = "/new";
    static final String URL_EDIT = "/edit";
    static final String ATTRIBUTE_OWNER = "owner";
    static final String ATTRIBUTE_SELECTIONS = "selections";

    private final OwnerService ownerService;

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    @GetMapping(URL_FIND)
    public String findOwners(Model model) {
        model.addAttribute(ATTRIBUTE_OWNER, Owner.builder().build());
        return VIEW_OWNER_FIND_FORM;
    }

    @GetMapping
    public String processFindForm(Owner owner, BindingResult result, Model model) {
        // allow parameterless GET request for /owners to return all records
        if (owner.getLastName() == null) {
            owner.setLastName(""); // empty string signifies broadest possible search
        }

        // find owners by last name
        Set<Owner> results = ownerService.findAllByLastNameLike("%" + owner.getLastName() + "%");

        if (results.isEmpty()) {
            // no owners found
            result.rejectValue("lastName", "notFound", "not found");
            return VIEW_OWNER_FIND_FORM;
        } else if (results.size() == 1) {
            // 1 owner found
            owner = results.iterator().next();
            return "redirect:" + URL_OWNERS + "/" + owner.getId();
        } else {
            // multiple owners found
            model.addAttribute(ATTRIBUTE_SELECTIONS, results);
            return VIEW_OWNERS_LIST;
        }
    }

    @GetMapping("/{ownerId}")
    public ModelAndView showOwner(@PathVariable Long ownerId) {
        ModelAndView mav = new ModelAndView(VIEW_OWNER_DETAILS);
        mav.addObject(ownerService.findById(ownerId));
        return mav;
    }

    @GetMapping(URL_NEW)
    public String initCreationForm(Model model) {
        model.addAttribute(ATTRIBUTE_OWNER, Owner.builder().build());
        return VIEW_OWNER_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping(URL_NEW)
    public String processCreationForm(@Valid Owner owner, BindingResult result) {
        if (result.hasErrors()) {
            return VIEW_OWNER_CREATE_OR_UPDATE_FORM;
        } else {
            Owner savedOwner = ownerService.save(owner);
            return "redirect:" + URL_OWNERS + "/" + savedOwner.getId();
        }
    }

    @GetMapping("/{ownerId}" + URL_EDIT)
    public String initUpdateOwnerForm(@PathVariable Long ownerId, Model model) {
        model.addAttribute(ownerService.findById(ownerId));
        return VIEW_OWNER_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping("/{ownerId}" + URL_EDIT)
    public String processUpdateOwnerForm(@Valid Owner owner, BindingResult result, @PathVariable Long ownerId) {
        if (result.hasErrors()) {
            return VIEW_OWNER_CREATE_OR_UPDATE_FORM;
        } else {
            owner.setId(ownerId);
            Owner savedOwner = ownerService.save(owner);
            return "redirect:" + URL_OWNERS + "/" + savedOwner.getId();
        }
    }
}
