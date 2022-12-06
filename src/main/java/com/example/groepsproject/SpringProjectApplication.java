package app;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@SpringBootApplication
@Controller
public class SpringProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringProjectApplication.class, args);
    }

    //Register

    @GetMapping ("/registerStudent")
    public String registrationStudent(Model model){
        model.addAttribute("student",new Student());
        return "/registerStudent";
    }

    @PostMapping ("/registerStudent")
    public String registrationStudentPost(@ModelAttribute Student student, Model model, RedirectAttributes redirAttrs) {
        try {
            App.app.registerStudent(student);
            redirAttrs.addFlashAttribute("success", "Student registered");
            return "redirect:/profileStudent";
        } catch (PersonException e) {
            redirAttrs.addFlashAttribute("failure", e.getMessage());
            return "redirect:/registerStudent";
        }
    }

    @GetMapping("/registerLandlord")
    public String registrationLandlord(Model model){
        model.addAttribute("landlord",new Landlord());
        return "/registerLandlord";
    }

    @PostMapping("/registerLandlord")
    public String registrationLandordPost(@ModelAttribute Landlord landlord, Model model, RedirectAttributes redirectAttributes){
        try{App.app.registerLandlord(landlord);
            redirectAttributes.addFlashAttribute("success","Landlord registered!");
            return "redirect:/profileLandlord";}
        catch (PersonException e){
            redirectAttributes.addFlashAttribute("failure", e.getMessage());
            return "redirect:/registerLandlord";
        }
    }



    //LOGIN

    @GetMapping("/profileStudent")
    public String getProfileStudent(Model model){
        PersonDAO personDAO = new PersonDAO();
        LandlordDAO landlordDAO = new LandlordDAO();
        model.addAttribute("loggedInStudent",App.app.getLoggedInStudent());
        model.addAttribute("name", App.app.getLoggedInStudent().getName());
        model.addAttribute("email", App.app.getLoggedInStudent().getEmail());
        model.addAttribute("telNumber", App.app.getLoggedInStudent().getTelNumber());
        model.addAttribute("password", App.app.getLoggedInStudent().getPassword());
        model.addAttribute("ID", App.app.getLoggedInStudent().getID());
        if(App.app.getLoggedInStudent().getRoom()!=null){
            model.addAttribute("roomID", App.app.getLoggedInStudent().getRoom().getAddress() + ", roomNr " + App.app.getLoggedInStudent().getRoom().getRoomNr());
            model.addAttribute("contactPerson", personDAO.getPerson(App.app.getLoggedInStudent().getRoom().getContactPersonID()).getEmail());
            model.addAttribute("landlord", landlordDAO.getLandlord(App.app.getLoggedInStudent().getRoom().getLandlordID()).getEmail());
        }
        else {
            model.addAttribute("roomID", "Will be assigned soon!");
            model.addAttribute("contactPerson", "Will be assigned soon!");
            model.addAttribute("landlord", "Will be assigned soon!");
        }
        return "/profileStudent";
    }

    @GetMapping("/loginLandlord")//inloggen landlord
    public String loginLandlord(Model model){
        return "/loginLandlord";
    }

    @PostMapping("/loginLandlord") //redirect naar appLandlord
    public String loginLandlordPost(@RequestParam("landlordID") int ID, @RequestParam("landlordPassword") String password, Model model, RedirectAttributes redirectAttributes) {
        try{LocalDate date = LocalDate.now();
            if (App.app.landlordPasswordCorrect(ID, password))
            {   if(date.getDayOfMonth()!=1)
            {App.app.logLandlordIn(ID);
                redirectAttributes.addFlashAttribute("success","Landlord logged in!");
                return "redirect:/profileLandlord";}

            else
            {   App.app.logLandlordIn(ID);
                redirectAttributes.addFlashAttribute("success","Landlord logged in!");
                return "redirect:/energyConsumption";}}
            else {
                throw new PersonException("ID and/or password are incorrect, try again!\nIf you don't have an account yet please register.");
            }
        }
        catch(PersonException e){
            redirectAttributes.addFlashAttribute("failure", e.getMessage());
            return "redirect:/loginLandlord";
        }
    }


    @GetMapping("/loginStudent") //inloggen student
    public String loginStudent(Model model){
        return "/loginStudent";
    }

    @PostMapping("/loginStudent") //redirect naar appStudent
    public String loginStudentPost(@RequestParam("studentID") int ID, @RequestParam("studentPassword") String studentPassword, Model model, RedirectAttributes redirectAttributes) {
        try {
            if (App.app.studentPasswordCorrect(ID, studentPassword)){
                App.app.logStudentIn(ID);
                redirectAttributes.addFlashAttribute("success","Student logged in!");
                return "redirect:/profileStudent";
            }
            else {
                throw new PersonException("ID and/or password are incorrect, try again!\nIf you don't have an account yet please register.");
            }
        }
        catch (PersonException e){
            redirectAttributes.addFlashAttribute("failure", e.getMessage());
            return "redirect:/loginStudent";
        }
    }


    //LANDLORD

    @GetMapping("/profileLandlord")
    public String profileLandlord(Model model){
        Landlord landlord = App.app.getLoggedInLandlord();
        model.addAttribute("loggedInLandlord", landlord);
        model.addAttribute("Name", landlord.getName());
        model.addAttribute("email", landlord.getEmail());
        model.addAttribute("telNumber", landlord.getTelNumber());
        model.addAttribute("password", landlord.getPassword());
        model.addAttribute("ID", landlord.getID());
        return "/profileLandlord";
    }

    @GetMapping("/setRoom")
    public String setRoom (Model model){
        return "/setRoom";
    }

    @PostMapping("/setRoom") //student toewijzen aan room
    public String setStudentToRoom( Model model, @RequestParam("studentID") Integer studentID,@RequestParam("roomAddress") String address,@RequestParam("roomNr")int roomNR, RedirectAttributes redirectAttributes){
        try{

            Student student = App.app.getStudent(studentID);
            Location room = App.app.getLocation(address, roomNR);
            if(room == null){
                throw new LocationException("The room you are trying to use doesn't exist.");
            }
            for (Location landlordRoom: App.app.getLoggedInLandlord().getRooms()){
                if (landlordRoom.equals(room)){
                    room.addStudenttoroom(student);
                    redirectAttributes.addFlashAttribute("success","Student assigned to room!");
                    return "redirect:/setRoom";
                }
            }
            throw new LocationException("The room you are trying to use doesn't exist.");
        }
        catch (PersonException | LocationException e){
            redirectAttributes.addFlashAttribute("failure", e.getMessage());
            return "redirect:/setRoom";
        }
    }

    @GetMapping("/addRoom") //nieuw location object toevoegen voor huidige landlord
    public String addRoom (Model model){
        model.addAttribute("room",new Location());
        return "/addRoom";
    }

    @PostMapping("/addRoom") // message naar GUI: nieuwe room toegevoegd// Naar waar doorverwijzen!?
    public String addRoomPost(@ModelAttribute("room") Location room, Model model, RedirectAttributes redirAttrs){
        try{
            App.app.getLoggedInLandlord().addRoom(room);
            redirAttrs.addFlashAttribute("success","Room has been added!");
        }

        catch(LocationException e){
            redirAttrs.addFlashAttribute("failure", e.getMessage());

        }
        return "redirect:/addRoom";
    }

    @GetMapping("/energyConsumption")
    public String setEnergyConsumption(Model model){
        model.addAttribute("energyConsumption",new MonthlyEnergyConsumption());
        return "/energyConsumption";
    }

    @PostMapping("/energyConsumption") // message naar GUI
    public String setEnergyConsumptionPost(@ModelAttribute("energyConsumption") MonthlyEnergyConsumption monthlyEnergyConsumption, @RequestParam("roomAddress") String roomAddress, @RequestParam("roomNr") int roomNr, Model model, RedirectAttributes redirAttrs){
        try{LocationDAO locationDAO = new LocationDAO();
            Location room = locationDAO.getLocation(roomAddress, roomNr);
            if (room == null){
                throw new LocationException("Room wasn't found");
            }
            App.app.getLoggedInLandlord().registerMonthlyEnergyConsumption(monthlyEnergyConsumption, room);
            redirAttrs.addFlashAttribute("success","Energy consumption filled in for this month.");}

        catch (LocationException e){
            redirAttrs.addFlashAttribute("failure", e.getMessage());
        }
        return "redirect:/energyConsumption";
    }
    //met REPORT zou ik nog even wachten om eerst te bekijken of we dat nu in een profiel weergeven ofzo

    @GetMapping("/reportElectricityLandlord")
    public String reportElectricityLandlord(Model model){
        Landlord landlord = App.app.getLoggedInLandlord();
        model.addAttribute("chartDataElec",App.app.graphDataElec());
        return "reportElectricityLandlord";
    }

    @GetMapping("/reportGasLandlord")
    public String reportGasLandlord(Model model){
        Landlord landlord = App.app.getLoggedInLandlord();
        model.addAttribute("chartDataGas",App.app.graphDataGas());
        return "reportGasLandlord";
    }

    @GetMapping("/reportWaterLandlord")
    public String reportLandlord(Model model){
        Landlord landlord = App.app.getLoggedInLandlord();
        model.addAttribute("chartDataWater",App.app.graphDataWater());
        return "reportWaterLandlord";
    }

    //hier moeten jullie maar eens laten weten welke attributes we er nu bij gingen zetten

    // ivm het verwijderen van een student uit een kamer, dus 'resetten' van room: ook aparte HTML en get -en postmapping?

    @GetMapping("/removeRoom") //wanneer kamer niet meer tot kamers van landlord behoort
    public String removeRoom(String adress){
        return "/removeRoom";
    }

    @PostMapping("/removeRoom") //message naar GUI: kamer verwijderd
    public String removeRoomPost(@RequestParam("roomAddress") String address,@RequestParam("roomNr")int roomNR,  Model model,RedirectAttributes redirAttrs ){
        try{
            Landlord landlord = App.app.getLoggedInLandlord();
            landlord.removeRoom(App.app.getLocation(address, roomNR));
            redirAttrs.addFlashAttribute("success","Room has been removed!");
        }
        catch (LocationException e){
            redirAttrs.addFlashAttribute("failure", e.getMessage());
        }
        return "redirect:/removeRoom";
    }


    @GetMapping("/createContactPerson")
    public String addContactPerson(Model model) {
        model.addAttribute("contactPerson", new Person());
        return "createContactPerson";
    }

    @PostMapping("/createContactPerson")
    public String addContactPersonToRoom(@ModelAttribute("contactPerson") Person contactPerson, Model model, RedirectAttributes redirectAttributes, @RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName, @RequestParam("telNumber") int telNumber, @RequestParam("email") String email){
        try{
            App.app.registerContactPerson(contactPerson);
            redirectAttributes.addFlashAttribute("success", "Contactperson was created, ID is " + contactPerson.getID());
        } catch (PersonException e) {
            redirectAttributes.addFlashAttribute("failure",e.getMessage());
        }
        return "redirect:/createContactPerson";
    }

    //STUDENT

    @GetMapping("/reportStudent")
    public String getAppliances (Model model, RedirectAttributes redirectAttributes){
        try{
            App.app.studentHasRoom();
            ConservActionDAO conservActionDAO = new ConservActionDAO();
            Student student = App.app.getLoggedInStudent();
            model.addAttribute("appliances", student.getRoom().getAppliances().values());
            model.addAttribute("mostRecommendedActions", conservActionDAO.get3HighestConservActions());
            model.addAttribute("graphData", App.app.usedConsumptionGraphStudent());
            return "/reportStudent";}
        catch (LocationException e){
            redirectAttributes.addFlashAttribute("failure", e.getMessage());
            return "redirect:/profileStudent";
        }
    }


    @GetMapping("/myAppliances")
    public String myAppliances (Model model, RedirectAttributes redirectAttributes){
        try{
            App.app.studentHasRoom();
            Student student = App.app.getLoggedInStudent();
            model.addAttribute("appliances", student.getRoom().getAppliances().values());
            return "/myAppliances";}
        catch (LocationException e){
            redirectAttributes.addFlashAttribute("failure", e.getMessage());
            return "redirect:/profileStudent";
        }
    }

    @GetMapping("/addAppliance") // toevoegen appliance
    public String addAppliance (Model model, RedirectAttributes redirectAttributes){
        try{
            App.app.studentHasRoom();
            Appliance appliance = new Appliance();
            model.addAttribute("Appliance",appliance);
            return "/addAppliance";}
        catch (LocationException e){
            redirectAttributes.addFlashAttribute("failure", e.getMessage());
            return "redirect:/profileStudent";
        }

    }

    @PostMapping("/addAppliance")
    public String addAppliancePost (Model model, @ModelAttribute ("Appliance") Appliance appliance, RedirectAttributes redirAttrs ){
        try{Student student = App.app.getLoggedInStudent();
            student.addAppliance(appliance);
            redirAttrs.addFlashAttribute("success", "Appliance has been added");
        }
        catch (ApplianceException | LocationException e){
            redirAttrs.addFlashAttribute("failure",e.getMessage());
        }
        return "redirect:/addAppliance";
    }


    @GetMapping("/removeAppliance") //verwijderen appliance
    public String removeAppliance (Model model, RedirectAttributes redirectAttributes){
        try{
            App.app.studentHasRoom();
            model.addAttribute("Appliance", new Appliance());
            return "/removeAppliance";}
        catch (LocationException e){
            redirectAttributes.addFlashAttribute("failure", e.getMessage());
            return "redirect:/profileStudent";
        }
    }

    @PostMapping ("/removeAppliance")
    public String removeAppliancePost (Model model, @ModelAttribute ("descriptionAppliance") String descriptionAppliance, RedirectAttributes redirectAttributes) {
        try {
            App.app.studentHasRoom();
            App.app.getLoggedInStudent().removeAppliance(descriptionAppliance);
            redirectAttributes.addFlashAttribute("success", "Appliance has been removed!");
        }
        catch (ApplianceException | LocationException e){
            redirectAttributes.addFlashAttribute("failure", e.getMessage());
        }
        return ("redirect:/removeAppliance");
    }

    //iets in aard van get preset ECAs om er uit te kunnen laten kiezen voor recommend en use

    @GetMapping("/recommendConservationAction") //bepaalde ECA aanraden --> smet ging deze tellen en dan weergeven welke gerecommend werden
    public String recommendECA (Model model, RedirectAttributes redirectAttributes){
        try{
            App.app.studentHasRoom();
            return ("/recommendConservationAction");}
        catch (LocationException e){
            redirectAttributes.addFlashAttribute("failure", e.getMessage());
            return "redirect:/profileStudent";
        }
    }

    @PostMapping("/recommendConservationAction") //zo kunnen wij lijst geven met recommended ECAs aan iemand die ECA wilt gebruiken
    public String recommendECAPost (Model model, @RequestParam ("Action") String Action , @RequestParam("ApplianceDescription") String appliancedescription, RedirectAttributes redirectAttributes){
        try{App.app.studentHasRoom();
            Student student = App.app.getLoggedInStudent();
            EnergyConservationAction ECA = student.getRoom().getAppliance(appliancedescription).getECA(Action);
            App.app.addRecommendation(ECA);
            redirectAttributes.addFlashAttribute("success", "Energy conservation action has been recommended!");
            return ("redirect:/recommendConservationAction");}
        catch (ApplianceException | ECAexception | LocationException e){
            redirectAttributes.addFlashAttribute("failure", e.getMessage());
            return ("redirect:/recommendConservationAction");
        }
    }

    @GetMapping("/useEnergyConservationAction") //aangeven welke energyconsaction ze gebruikt hebben
    public String useECA (Model model, RedirectAttributes redirectAttributes){
        try{
            App.app.studentHasRoom();
            model.addAttribute("allEnergyConservationActions", App.app.getLoggedInStudent().getAllConservationActions());
            return ("/useEnergyConservationAction");}

        catch (LocationException e){
            redirectAttributes.addFlashAttribute("failure", e.getMessage());
            return "redirect:/profileStudent";
        }
    }

    @PostMapping ("/useEnergyConservationAction")
    public String useECAPost (@RequestParam ("Action") String Action , @RequestParam("ApplianceDescription") String appliancedescription, Model model, RedirectAttributes redirectAttributes ){
        try{
            Student student = App.app.getLoggedInStudent();
            Appliance appliance = student.getRoom().getAppliance(appliancedescription);
            EnergyConservationAction ECA = appliance.getECA(Action);
            student.useConservationAction(ECA, appliance);
            redirectAttributes.addFlashAttribute("success", "Energy Conservation action has been used!");
            return ("redirect:/useEnergyConservationAction");
        }
        catch (ApplianceException | ECAexception e){
            redirectAttributes.addFlashAttribute("failure", e.getMessage());
            return ("redirect:/useEnergyConservationAction");
        }
    }

    @GetMapping ("/getUsedConservationActions")
    public String getUsedConservationActions (Model model, RedirectAttributes redirectAttributes){
        try{
            App.app.studentHasRoom();
            model.addAttribute("usedEnergyConservationActions", App.app.getLoggedInStudent().getUsedEnergyConservationActions());
            return "/getUsedConservationActions";}
        catch (LocationException e){
            redirectAttributes.addFlashAttribute("failure", e.getMessage());
            return "redirect:/profileStudent";
        }
    }

    @GetMapping("/getAllMyConservationActions")
    public String getAllConservationsActions(Model model, RedirectAttributes redirectAttributes){
        try {
            App.app.studentHasRoom();
            model.addAttribute("allEnergyConservationActions", App.app.getLoggedInStudent().getAllConservationActions());
            return "/getAllMyConservationActions";}
        catch (LocationException e){
            redirectAttributes.addFlashAttribute("failure", e.getMessage());
            return "redirect:/profileStudent";
        }
    }


    @GetMapping("/addEnergyConservationAction")
    public String addECA (Model model, RedirectAttributes redirectAttributes){
        try{
            App.app.studentHasRoom();
            model.addAttribute("EnergyConservationAction", new EnergyConservationAction());
            model.addAttribute("preSetActions", App.app.getLoggedInStudent().getPreSetActions());
            return "/addEnergyConservationAction";}
        catch(LocationException e){
            redirectAttributes.addFlashAttribute("failure", e.getMessage());
            return "redirect:/profileStudent";
        }
    }

    @PostMapping("/addEnergyConservationAction")
    public String addECAPost (Model model, @ModelAttribute("EnergyConservationAction") EnergyConservationAction energyConservationAction, RedirectAttributes redirectAttributes, @RequestParam("applianceDescription") String applianceDescription) {
        try{
            if (App.app.getLoggedInStudent().getRoom() == null){
                throw new LocationException("You don't have a room yet please wait until your landlord assigns you to one.");
            }
            App.app.getLoggedInStudent().getRoom().getAppliance(applianceDescription).addEnergyConservationAction(energyConservationAction);
            redirectAttributes.addFlashAttribute("success", "Energy conservation action " +energyConservationAction.getAction() +  " has been added to appliance " + applianceDescription +"!");}
        catch (ApplianceException | ECAexception e){
            redirectAttributes.addFlashAttribute("failure", e.getMessage());
        }
        catch (LocationException e){
            redirectAttributes.addFlashAttribute("failure", e.getMessage());
            return "redirect:/profilStudent";
        }
        return "redirect:/addEnergyConservationAction";
    }



    //logOut
    @GetMapping("/logoutStudent")
    public String logoutStudent (Model model){
        App.app.logoutStudent();
        return "redirect:/loginStudent";
    }

    @GetMapping("/logoutLandlord")
    public String logoutLandlord (Model model){
        App.app.logoutLandlord();
        return "redirect:/loginLandlord";
    }

    //PROBEERSEL STUDENT REPORT
}
