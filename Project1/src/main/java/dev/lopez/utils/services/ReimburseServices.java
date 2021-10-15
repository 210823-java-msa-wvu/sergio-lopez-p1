package dev.lopez.utils.services;

;
import dev.lopez.utils.dao.EmployeeDao;
import dev.lopez.utils.dao.ManagerDao;
import dev.lopez.utils.dao.ReimburseDao;
import dev.lopez.utils.exceptions.InsufficientBalanceException;
import dev.lopez.utils.exceptions.NoPermissionException;
import dev.lopez.utils.exceptions.NotYoursToSignException;
import dev.lopez.utils.exceptions.StartDatePassedException;
import dev.lopez.utils.model.Employee;
import dev.lopez.utils.model.Manager;
import dev.lopez.utils.model.Reimburse;


import java.time.LocalDate;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

public class ReimburseServices {
    private static final ReimburseDao reimbDao = new ReimburseDao();
    private static final ManagerDao managerDao = new ManagerDao();
    private static final EmployeeDao empDao = new EmployeeDao();
    private static final float []coverage_lookup = {0.8f,0.6f,0.75f,1f,0.9f,0.3f};

    public Reimburse submitReimburse(Employee emp, LocalDate start, int type, float cost, int grade_scheme, int pass, String note, String location)
            throws InsufficientBalanceException, StartDatePassedException{
        float amount = cost * coverage_lookup[type];
        if(emp.getAwarded()+emp.getPending()+amount >1000){
            throw new InsufficientBalanceException();
        }
        if(grade_scheme==7) pass = 0;
        LocalDate subDate = LocalDate.now();
        //submit date has passed start date
        if(subDate.compareTo(start)>=0) throw new StartDatePassedException();
        //urgent if within 14 days, pending otherwise
        int status = DAYS.between(subDate,start)>=14? 0:3;
        int signer = emp.getReport_to();
        Reimburse reimb = new Reimburse();
        reimb.setType(type);
        reimb.setApplicant(emp.getId());
        reimb.setAmount(amount);
        reimb.setStart_date(start);
        reimb.setSubmit_date(subDate);
        reimb.setPass_grade(pass);
        reimb.setGrade_scheme(grade_scheme);
        reimb.setNext_signer(signer);
        reimb.setStatus(status);
        reimb.setNote(note);
        reimb.setLocation(location);
        emp.setPending(emp.getPending()+amount);
        empDao.updateEmployeePending(emp);
        return reimbDao.addReimburse(reimb);
    }

    public Reimburse signReimburse(Manager m, int id) throws NotYoursToSignException {
        Reimburse reimb = reimbDao.getReimburseById(id);
        if(m.getId()!=reimb.getNext_signer()) throw new NotYoursToSignException();
        if(m.getRank()==3){
            reimb.setLevel3(m.getId());
            reimb.setNext_signer(-1);
            reimb.setStatus(7); //justo set status to ranted if you can't figure it out
            reimb.setSign_date(LocalDate.now());
            reimbDao.updateReimburseSigner(reimb);
            reimbDao.updateReimburseStatus(reimb);
        }
        else if(m.getRank()==2){
            //direct report to department head
            if(reimb.getLevel1()==-1){
                reimb.setLevel2(m.getId());
                reimb.setLevel1(m.getId());
                reimb.setNext_signer(m.getReport_to());
                reimb.setSign_date(LocalDate.now());
                reimbDao.updateReimburseSigner(reimb);
                reimb.setStatus(0);
                reimbDao.updateReimburseStatus(reimb);
            }
            else{
                reimb.setLevel2(m.getId());
                reimb.setNext_signer(m.getReport_to());
                reimb.setSign_date(LocalDate.now());
                reimbDao.updateReimburseSigner(reimb);
                reimb.setStatus(0);
                reimbDao.updateReimburseStatus(reimb);
            }
        }
        else{
            reimb.setLevel1(m.getId());//benco finally approves
            reimb.setNext_signer(m.getReport_to());
            reimb.setSign_date(LocalDate.now());
            reimbDao.updateReimburseSigner(reimb);
        }
        return reimbDao.getReimburseById(reimb.getId());
    }

    public Reimburse declineReimburse(Manager m, int id, String reason) throws NotYoursToSignException{
        Reimburse reimb = reimbDao.getReimburseById(id);
        //cannot decline anything that has been declined or granted
        if(reimb.getStatus()==4||reimb.getStatus()==5) throw new NotYoursToSignException();
        //not benco and not signing and not the person confirming presentation for the reimbursement
        if(m.getRank()!=3&&m.getId()!=reimb.getNext_signer()&&!(reimb.getGrade_scheme()==7&&m.getId()==reimb.getLevel1()&&reimb.getScore()>=0)) throw new NoPermissionException();
        Employee emp = empDao.getEmployeeById(reimb.getApplicant());
        StringBuilder sb = new StringBuilder(reimb.getNote());
        sb.append("\n<br>Declined by ").append(m.getFirst()).append(" ").append(m.getLast()).append(": ").append(reason);
        String new_note = sb.toString();
        reimb.setNote(new_note);
        reimb.setStatus(4);
        reimbDao.updateReimburseStatus(reimb);
        reimbDao.updateReimburseNote(reimb);
        emp.setPending(emp.getPending()-reimb.getAmount());
        empDao.updateEmployeePending(emp);
        return reimbDao.getReimburseById(reimb.getId());
    }

    public Reimburse declineReimburse(int emp_id, int id) throws NotYoursToSignException{
        Employee emp = empDao.getEmployeeById(emp_id);
        Reimburse reimb = reimbDao.getReimburseById(id);
        if(emp.getId()!=reimb.getApplicant()||reimb.getStatus()!=2) throw new NotYoursToSignException();
        StringBuilder sb = new StringBuilder(reimb.getNote());
        sb.append("\n<br>Declined by ").append(emp.getFirst()).append(" ").append(emp.getLast());
        String new_note = sb.toString();
        reimb.setNote(new_note);
        reimb.setStatus(4);
        reimbDao.updateReimburseStatus(reimb);
        reimbDao.updateReimburseNote(reimb);
        emp.setPending(emp.getPending()-reimb.getAmount());
        empDao.updateEmployeePending(emp);
        return reimbDao.getReimburseById(reimb.getId());
    }


    public void updateReimbursement(){
        //get open tickets
        List<Reimburse> list = reimbDao.getAllOpenReimburse();
        //update each entry
        for(Reimburse reimb : list){
            LocalDate today = LocalDate.now();
            LocalDate sign = reimb.getSign_date();
            Manager signer = managerDao.getManagerById(reimb.getNext_signer());
            //not signed by anyone yet
            if(sign==null){
                LocalDate subDate = reimb.getSubmit_date();
                //check submit date, auto sign if 3 or more days
                if(DAYS.between(reimb.getSubmit_date(),today)>=3&&(reimb.getStatus()==0||reimb.getStatus()==3)){
                    Employee emp = empDao.getEmployeeById(reimb.getApplicant());
                    Manager m = managerDao.getManagerById(emp.getReport_to());
                    reimb = signReimburse(m,reimb.getId());
                    reimb.setNote(reimb.getNote()+"\n<br>AutoSign by: "+m.getFirst()+" "+m.getLast());
                    reimbDao.updateReimburseNote(reimb);
                }
                //else do nothing
            }
            //sign date is not null, check if last sign date is 3 or more and not BENCO
            else if(DAYS.between(sign,today)>=3&&signer.getRank()<3&&(reimb.getStatus()==0||reimb.getStatus()==3)){
                Manager m = managerDao.getManagerById(reimb.getNext_signer());
                reimb = signReimburse(m,reimb.getId());
                reimb.setNote(reimb.getNote()+"\n<br>AutoSign by: "+m.getFirst()+" "+m.getLast());
                reimbDao.updateReimburseNote(reimb);
            }
            //last sign date is 3 or more and is BENCO
            else if(DAYS.between(sign,today)>=3&&(reimb.getStatus()==0||reimb.getStatus()==3)){
                String note = reimb.getNote();
                //haven't sent email
                if(!note.contains("Lazy BENCO: email sent")){
                    reimb.setNote(note+"\n<br>Lazy BENCO: email sent");
                    reimbDao.updateReimburseNote(reimb);
                }
            }
            //else do nothing
            //check if past start date and not in grade submission/complete stage
            if(today.compareTo(reimb.getStart_date())>=0&&
               (reimb.getStatus()!=7||reimb.getStatus()!=8||reimb.getStatus()!=4||reimb.getStatus()!=5)){
                reimb.setStatus(4);
                reimb.setNote(reimb.getNote()+"\n<br>Past start date, auto declined");
                reimbDao.updateReimburseStatus(reimb);
                reimbDao.updateReimburseNote(reimb);
            }
            //check if urgent
            else if(DAYS.between(today,reimb.getStart_date())<14&&reimb.getStatus()==0){
                reimb.setStatus(3);
                reimbDao.updateReimburseStatus(reimb);
            }
            //else do nothing
        }
    }

    public List<Reimburse> getAllReimbursement(){return reimbDao.getAllReimburse();}

    public List<Reimburse> getReimburseByApplicant(int id){
        return reimbDao.getReimbursesByApplicant(id);
    }

    public List<Reimburse> getReimburseForManager(Manager m){
        return reimbDao.getReimbursesForManager(m);
    }

    public List<Reimburse> getReimburseForReward(Manager m){
        return reimbDao.getReimburseForGrant(m);
    }

    public List<Reimburse> getReimburseExceed(){
        return reimbDao.getReimburseExceed();
    }

    public Reimburse changeAmount(Manager m, int id, float amount, String reason) throws NoPermissionException{
        Reimburse reimb = reimbDao.getReimburseById(id);
        if(m.getRank()<3) throw new NoPermissionException();
        if(reimb.getStatus()==4||reimb.getStatus()==5) throw new NoPermissionException();
        reimb.setNote(reimb.getNote()+"\n<br>Amount changed by "+m.getFirst()+" "+m.getLast()+": "+reason);
        reimb.setStatus(2);
        float change = amount - reimb.getAmount();
        reimb.setAmount(amount);
        Employee emp = empDao.getEmployeeById(reimb.getApplicant());
        //mark as exceeding reimbursement
        if(emp.getPending()+emp.getAwarded()+change >1000) reimb.setExceed(true);
        emp.setPending(emp.getPending()+change);
        empDao.updateEmployeePending(emp);
        reimbDao.updateReimburseStatus(reimb);
        reimbDao.updateReimburseAmount(reimb);
        reimbDao.updateReimburseNote(reimb);
        return reimb;
    }

    public Reimburse confirmGrade(Manager m, int id){
        Reimburse reimb = reimbDao.getReimburseById(id);
        if(reimb.getStatus()==4||reimb.getStatus()==5) throw new NoPermissionException();
        Employee emp = empDao.getEmployeeById(reimb.getApplicant());
        //presentation and direct manager and presentation submitted
        if(reimb.getGrade_scheme()==7&&m.getId()==emp.getReport_to()&&reimb.getScore()>=0){
            emp.setAwarded(emp.getAwarded()+reimb.getAmount());
            emp.setPending(emp.getPending()-reimb.getAmount());
            empDao.updateEmployeeAwarded(emp);
            empDao.updateEmployeePending(emp);
            reimb.setStatus(5);
            reimb.setNote(reimb.getNote()+"\n<br>Granted");
            reimbDao.updateReimburseNote(reimb);
            reimbDao.updateReimburseStatus(reimb);
            return reimb;
        }
        //grade and BENCO and grade passed
        else if(reimb.getGrade_scheme()==5&&m.getRank()==3&&reimb.getScore()>=reimb.getPass_grade()){
            emp.setAwarded(emp.getAwarded()+reimb.getAmount());
            emp.setPending(emp.getPending()-reimb.getAmount());
            empDao.updateEmployeeAwarded(emp);
            empDao.updateEmployeePending(emp);
            reimb.setStatus(5);
            reimb.setNote(reimb.getNote()+"\n<br>Granted");
            reimbDao.updateReimburseNote(reimb);
            reimbDao.updateReimburseStatus(reimb);
            return reimb;
        }
        else throw new NotYoursToSignException();
    }

    public Reimburse requestInfo(Manager m, int id,int level){
        Reimburse reimb = reimbDao.getReimburseById(id);
        if(m.getId()!=reimb.getNext_signer()||m.getRank()<=level) throw new NoPermissionException();
        if(level ==0) {
            reimb.setNote(reimb.getNote() + "\n<br>Request Info from employee.");
            reimb.setStatus(1);
            reimbDao.updateReimburseStatus(reimb);
            reimbDao.updateReimburseNote(reimb);
            return reimb;
        }
        if(level ==1){
            reimb.setNote(reimb.getNote() + "\n<br>Request Info from direct manager.");
            reimb.setStatus(6);
            if(reimb.getLevel1()==-1) throw new NoPermissionException();
            reimb.setNext_info(reimb.getLevel1());
            reimbDao.updateReimburseNote(reimb);
            reimbDao.updateReimburseStatus(reimb);
            return reimb;
        }
        if(level == 2){
            reimb.setNote(reimb.getNote() + "\n<br>Request Info from department head.");
            reimb.setStatus(6);
            reimb.setNext_info(reimb.getLevel2());
            if(reimb.getLevel2()==-1) throw new NoPermissionException();
            reimbDao.updateReimburseNote(reimb);
            reimbDao.updateReimburseStatus(reimb);
            return reimb;
        }
        return null;
    }

    public Reimburse uploadInfo(Manager m, int id){
        Reimburse reimb=reimbDao.getReimburseById(id);
        if(m.getId()!=reimb.getNext_info()) throw new NoPermissionException();
        reimb.setNote(reimb.getNote()+" Info provided by: "+m.getFirst()+" "+m.getLast());
        reimb.setNext_info(-1);
        reimb.setStatus(0);
        reimbDao.updateReimburseStatus(reimb);
        reimbDao.updateReimburseNote(reimb);
        return reimb;
    }

    public Reimburse uploadInfo(Employee emp, int id){
        Reimburse reimb=reimbDao.getReimburseById(id);
        if(emp.getId()!=reimb.getApplicant()||reimb.getStatus()!=1) throw new NoPermissionException();
        reimb.setNote(reimb.getNote()+" Info provided by: "+emp.getFirst()+" "+emp.getLast());
        reimb.setStatus(0);
        reimbDao.updateReimburseStatus(reimb);
        reimbDao.updateReimburseNote(reimb);
        return reimb;
    }

    public Reimburse uploadGrade(Employee emp, int id, int score){
        Reimburse reimb = reimbDao.getReimburseById(id);
        if(emp.getId()!=reimb.getApplicant()||reimb.getGrade_scheme()!=5||reimb.getLevel3()==-1) throw new NoPermissionException();
        reimb.setScore(score);
        reimb.setNote(reimb.getNote()+"<br>\nGrade uploaded");
        reimb.setStatus(8);
        reimbDao.updateReimburseStatus(reimb);
        reimbDao.updateReimburseNote(reimb);
        reimbDao.updateReimburseScore(reimb);
        return reimb;
    }

    public Reimburse uploadPresentation(Employee emp, int id){
        Reimburse reimb = reimbDao.getReimburseById(id);
        if(emp.getId()!=reimb.getApplicant()||reimb.getGrade_scheme()!=7||reimb.getLevel3()==-1) throw new NoPermissionException();
        reimb.setScore(100);
        reimb.setNote(reimb.getNote()+"<br>\nPresentation uploaded");
        reimb.setStatus(8);
        reimbDao.updateReimburseStatus(reimb);
        reimbDao.updateReimburseNote(reimb);
        reimbDao.updateReimburseScore(reimb);
        return reimb;
    }
}
