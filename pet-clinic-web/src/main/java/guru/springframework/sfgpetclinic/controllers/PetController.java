package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.model.Pet;
import guru.springframework.sfgpetclinic.model.PetType;
import guru.springframework.sfgpetclinic.services.OwnerService;
import guru.springframework.sfgpetclinic.services.PetService;
import guru.springframework.sfgpetclinic.services.PetTypeService;
import guru.springframework.sfgpetclinic.validators.PetValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

import static guru.springframework.sfgpetclinic.controllers.ControllerUtils.URL_EDIT;
import static guru.springframework.sfgpetclinic.controllers.ControllerUtils.URL_NEW;
import static guru.springframework.sfgpetclinic.controllers.OwnerController.URL_OWNERS;

/**
 * @author Gaetan Bloch
 * Created on 25/03/2020
 */
@RequiredArgsConstructor
@Controller
@RequestMapping(OwnerController.URL_OWNERS + "/{ownerId}")
final class PetController {
    static final String VIEW_PETS_CREATE_OR_UPDATE_FORM = "pets/createOrUpdatePetForm";
    static final String URL_PETS = "/pets";
    static final String URL_PETS_NEW = URL_PETS + URL_NEW;
    static final String URL_REDIRECT_OWNER = "redirect:" + URL_OWNERS + "/";
    static final String URL_PET_EDIT = URL_PETS + "/{petId}" + URL_EDIT;
    static final String ATTRIBUTE_PET = "pet";

    private final PetService petService;
    private final OwnerService ownerService;
    private final PetTypeService petTypeService;

    @ModelAttribute("types")
    public Set<PetType> populatePetTypes() {
        return petTypeService.findAll();
    }

    @ModelAttribute("owner")
    public Owner findOwner(@PathVariable("ownerId") Long ownerId) {
        return ownerService.findById(ownerId);
    }

    @InitBinder("owner")
    public void initOwnerBinder(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    @InitBinder("pet")
    public void initPetBinder(WebDataBinder dataBinder) {
        dataBinder.setValidator(new PetValidator());
    }

    @GetMapping(URL_PETS_NEW)
    public String initCreationForm(Owner owner, Model model) {
        Pet pet = new Pet();
        owner.getPets().add(pet);
        pet.setOwner(owner);
        model.addAttribute(ATTRIBUTE_PET, pet);
        return VIEW_PETS_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping(URL_PETS_NEW)
    public String processCreationForm(Owner owner, @Valid Pet pet, BindingResult result, Model model) {
        if (StringUtils.hasLength(pet.getName()) &&
                pet.isNew() &&
                owner.getPet(pet.getName(), true) != null) {
            result.rejectValue("name", "duplicate", "already exists");
        }
        owner.getPets().add(pet);
        pet.setOwner(owner);
        if (result.hasErrors()) {
            model.addAttribute(ATTRIBUTE_PET, pet);
            return VIEW_PETS_CREATE_OR_UPDATE_FORM;
        } else {
            petService.save(pet);
            return URL_REDIRECT_OWNER + owner.getId();
        }
    }

    @GetMapping(URL_PET_EDIT)
    public String initUpdateForm(@PathVariable Long petId, Model model) {
        model.addAttribute(ATTRIBUTE_PET, petService.findById(petId));
        return VIEW_PETS_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping(URL_PET_EDIT)
    public String processUpdateForm(@Valid Pet pet, BindingResult result, Owner owner, Model model) {
        pet.setOwner(owner);
        if (result.hasErrors()) {
            model.addAttribute(ATTRIBUTE_PET, pet);
            return VIEW_PETS_CREATE_OR_UPDATE_FORM;
        } else {
            petService.save(pet);
            return URL_REDIRECT_OWNER + owner.getId();
        }
    }
}
