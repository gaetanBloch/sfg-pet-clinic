package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.model.Pet;
import guru.springframework.sfgpetclinic.model.Visit;
import guru.springframework.sfgpetclinic.services.OwnerService;
import guru.springframework.sfgpetclinic.services.PetService;
import guru.springframework.sfgpetclinic.services.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.beans.PropertyEditorSupport;
import java.time.LocalDate;

import static guru.springframework.sfgpetclinic.controllers.ControllerUtils.URL_NEW;
import static guru.springframework.sfgpetclinic.controllers.OwnerController.URL_OWNERS;
import static guru.springframework.sfgpetclinic.controllers.PetController.*;

/**
 * @author Gaetan Bloch
 * Created on 25/03/2020
 */
@RequiredArgsConstructor
@Controller
final class VisitController {
    static final String URL_VISITS = "/visits";
    static final String VIEW_VISIT_CREATE_OR_UPDATE_FORM = "pets/createOrUpdateVisitForm";
    static final String ATTRIBUTE_VISIT = "visit";

    private final VisitService visitService;
    private final PetService petService;
    private final OwnerService ownerService;

    @InitBinder
    public void dataBinder(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
        dataBinder.registerCustomEditor(LocalDate.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                setValue(LocalDate.parse(text));
            }
        });
    }

    /**
     * Called before each and every @RequestMapping annotated method.
     * 2 goals:
     * - Make sure we always have fresh data
     * - Since we do not use the session scope, make sure that Pet object always has an id
     * (Even though id is not part of the form fields)
     *
     * @param petId pet id
     * @return Pet
     */
    @ModelAttribute(ATTRIBUTE_VISIT)
    public Visit loadPetWithVisit(@PathVariable("petId") Long petId, Model model) {
        Pet pet = petService.findById(petId);
        model.addAttribute(ATTRIBUTE_PET, pet);
        Visit visit = new Visit();
        pet.getVisits().add(visit);
        visit.setPet(pet);
        return visit;
    }

    // Spring MVC calls method loadPetWithVisit(...) before initNewVisitForm is called
    @GetMapping(URL_OWNERS + "/*" + URL_PETS + "/{petId}" + URL_VISITS + URL_NEW)
    public String initNewVisitForm() {
        return VIEW_VISIT_CREATE_OR_UPDATE_FORM;
    }

    // Spring MVC calls method loadPetWithVisit(...) before processNewVisitForm is called
    @PostMapping(URL_OWNERS + "/{ownerId}" + URL_PETS + "/{petId}" + URL_VISITS + URL_NEW)
    public String processNewVisitForm(@Valid Visit visit, BindingResult result, @PathVariable Long ownerId) {
        Owner owner = ownerService.findById(ownerId);
        if (result.hasErrors()) {
            return VIEW_VISIT_CREATE_OR_UPDATE_FORM;
        } else {
            visitService.save(visit);
            return URL_REDIRECT_OWNER + owner.getId();
        }
    }
}
