package dev.lopez.utils.dao;

import dev.lopez.utils.model.Manager;
import dev.lopez.utils.model.Reimburse;
import dev.lopez.utils.utils.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReimburseDao {
    private ConnectionUtil cu = ConnectionUtil.getConnectionUtil();

    //Read
    public Reimburse getReimburseById(Integer id){
        //try with resources initialize resource that will be closed after we complete
        try(Connection con = cu.getConnection()){
            String sql = "select * from \"TRMS1.3\".reimbursements where id=?";

            //avoid sql injection attacks
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1,id);

            ResultSet result = ps.executeQuery();

            if(result.next()){
                Reimburse reimb = new Reimburse();
                reimb.setId(id);
                reimb.setType(result.getInt("type"));
                reimb.setAmount(result.getFloat("amount"));
                reimb.setApplicant(result.getInt("applicant"));
                reimb.setPass_grade(result.getInt("pass_grade"));
                reimb.setGrade_scheme(result.getInt("grade_scheme"));
                reimb.setStart_date(result.getObject("start_date", LocalDate.class));
                reimb.setSubmit_date(result.getObject("submit_date",LocalDate.class));
                reimb.setSign_date(result.getObject("sign_date", LocalDate.class));
                reimb.setLevel1(result.getInt("level1"));
                reimb.setLevel2(result.getInt("level2"));
                reimb.setLevel3(result.getInt("level3"));
                reimb.setNext_signer(result.getInt("next_signer"));
                reimb.setSign_date(result.getObject("sign_date",LocalDate.class));
                reimb.setStatus(result.getInt("status"));
                reimb.setNote(result.getString("note"));
                reimb.setLocation(result.getString("location"));
                reimb.setScore(result.getInt("score"));
                reimb.setExceed(result.getBoolean("exceed"));
                reimb.setNext_info(result.getInt("next_info"));
                return reimb;
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }


    public List<Reimburse> getReimbursesForManager(Manager m){
        //try with resources initialize resource that will be closed after we complete
        try(Connection con = cu.getConnection()){
            List<Reimburse> reimburses = new ArrayList<Reimburse>();
            String sql = "select * from \"TRMS1.3\".reimbursements where (next_signer=? or next_info=?) and status!=4 and status!=5";

            //avoid sql injection attacks
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1,m.getId());
            ps.setInt(2,m.getId());

            ResultSet result = ps.executeQuery();

            while(result.next()){
                Reimburse reimb = new Reimburse();
                reimb.setId(result.getInt("id"));
                reimb.setType(result.getInt("type"));
                reimb.setApplicant(result.getInt("applicant"));
                reimb.setAmount(result.getFloat("amount"));
                reimb.setPass_grade(result.getInt("pass_grade"));
                reimb.setGrade_scheme(result.getInt("grade_scheme"));
                reimb.setStart_date(result.getObject("start_date",LocalDate.class));
                reimb.setSubmit_date(result.getObject("submit_date",LocalDate.class));
                reimb.setSign_date(result.getObject("sign_date", LocalDate.class));
                reimb.setLevel1(result.getInt("level1"));
                reimb.setLevel2(result.getInt("level2"));
                reimb.setLevel3(result.getInt("level3"));
                reimb.setNext_signer(result.getInt("next_signer"));
                reimb.setSign_date(result.getObject("sign_date",LocalDate.class));
                reimb.setStatus(result.getInt("status"));
                reimb.setNote(result.getString("note"));
                reimb.setLocation(result.getString("location"));
                reimb.setScore(result.getInt("score"));
                reimb.setExceed(result.getBoolean("exceed"));
                reimb.setNext_info(result.getInt("next_info"));
                reimburses.add(reimb);
            }
            return reimburses;
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public List<Reimburse> getReimbursesByApplicant(int id){
        //try with resources initialize resource that will be closed after we complete
        try(Connection con = cu.getConnection()){
            List<Reimburse> reimburses = new ArrayList<Reimburse>();
            String sql = "select * from \"TRMS1.3\".reimbursements where applicant=?";

            //avoid sql injection attacks
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1,id);

            ResultSet result = ps.executeQuery();

            while(result.next()){
                Reimburse reimb = new Reimburse();
                reimb.setId(result.getInt("id"));
                reimb.setType(result.getInt("type"));
                reimb.setApplicant(id);
                reimb.setAmount(result.getFloat("amount"));
                reimb.setPass_grade(result.getInt("pass_grade"));
                reimb.setGrade_scheme(result.getInt("grade_scheme"));
                reimb.setStart_date(result.getObject("start_date",LocalDate.class));
                reimb.setSubmit_date(result.getObject("submit_date",LocalDate.class));
                reimb.setSign_date(result.getObject("sign_date", LocalDate.class));
                reimb.setLevel1(result.getInt("level1"));
                reimb.setLevel2(result.getInt("level2"));
                reimb.setLevel3(result.getInt("level3"));
                reimb.setNext_signer(result.getInt("next_signer"));
                reimb.setSign_date(result.getObject("sign_date",LocalDate.class));
                reimb.setStatus(result.getInt("status"));
                reimb.setNote(result.getString("note"));
                reimb.setLocation(result.getString("location"));
                reimb.setScore(result.getInt("score"));
                reimb.setExceed(result.getBoolean("exceed"));
                reimb.setNext_info(result.getInt("next_info"));
                reimburses.add(reimb);
            }
            return reimburses;
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public List<Reimburse> getReimburseExceed(){
        //try with resources initialize resource that will be closed after we complete
        try(Connection con = cu.getConnection()){
            List<Reimburse> reimburses = new ArrayList<Reimburse>();
            String sql = "select * from \"TRMS1.3\".Reimbursements where exceed=true";

            //avoid sql injection attacks
            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet result = ps.executeQuery();

            while(result.next()){
                Reimburse reimb = new Reimburse();
                reimb.setId(result.getInt("id"));
                reimb.setApplicant(result.getInt("applicant"));
                reimb.setType(result.getInt("type"));
                reimb.setAmount(result.getFloat("amount"));
                reimb.setPass_grade(result.getInt("pass_grade"));
                reimb.setGrade_scheme(result.getInt("grade_scheme"));
                reimb.setStart_date(result.getObject("start_date",LocalDate.class));
                reimb.setSubmit_date(result.getObject("submit_date",LocalDate.class));
                reimb.setSign_date(result.getObject("sign_date", LocalDate.class));
                reimb.setLevel1(result.getInt("level1"));
                reimb.setLevel2(result.getInt("level2"));
                reimb.setLevel3(result.getInt("level3"));
                reimb.setNext_signer(result.getInt("next_signer"));
                reimb.setStatus(result.getInt("status"));
                reimb.setNote(result.getString("note"));
                reimb.setLocation(result.getString("location"));
                reimb.setScore(result.getInt("score"));
                reimb.setExceed(result.getBoolean("exceed"));
                reimb.setNext_info(result.getInt("next_info"));
                reimburses.add(reimb);
            }
            return reimburses;
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public List<Reimburse> getReimburseForGrant(Manager m){
        //try with resources initialize resource that will be closed after we complete
        try(Connection con = cu.getConnection()){
            List<Reimburse> reimburses = new ArrayList<Reimburse>();
            if(m.getRank()==3) {
                //is grade
                String sql = "select * from \"TRMS1.3\".Reimbursements where status=8 and grade_scheme=5";

                //avoid sql injection attacks
                PreparedStatement ps = con.prepareStatement(sql);

                ResultSet result = ps.executeQuery();

                while (result.next()) {
                    Reimburse reimb = new Reimburse();
                    reimb.setId(result.getInt("id"));
                    reimb.setType(result.getInt("type"));
                    reimb.setApplicant(result.getInt("applicant"));
                    reimb.setAmount(result.getFloat("amount"));
                    reimb.setPass_grade(result.getInt("pass_grade"));
                    reimb.setGrade_scheme(result.getInt("grade_scheme"));
                    reimb.setStart_date(result.getObject("start_date", LocalDate.class));
                    reimb.setSubmit_date(result.getObject("submit_date", LocalDate.class));
                    reimb.setSign_date(result.getObject("sign_date", LocalDate.class));
                    reimb.setLevel1(result.getInt("level1"));
                    reimb.setLevel2(result.getInt("level2"));
                    reimb.setLevel3(result.getInt("level3"));
                    reimb.setNext_signer(result.getInt("next_signer"));
                    reimb.setStatus(result.getInt("status"));
                    reimb.setNote(result.getString("note"));
                    reimb.setLocation(result.getString("location"));
                    reimb.setScore(result.getInt("score"));
                    reimb.setExceed(result.getBoolean("exceed"));
                    reimb.setNext_info(result.getInt("next_info"));
                    reimburses.add(reimb);
                }
            }
            else{
                //is presentation
                String sql = "select * from \"TRMS1.3\".Reimbursements where status=8 and grade_scheme=7 and level1=?";

                //avoid sql injection attacks
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1,m.getId());
                ResultSet result = ps.executeQuery();

                while (result.next()) {
                    Reimburse reimb = new Reimburse();
                    reimb.setId(result.getInt("id"));
                    reimb.setType(result.getInt("type"));
                    reimb.setAmount(result.getFloat("amount"));
                    reimb.setApplicant(result.getInt("applicant"));
                    reimb.setPass_grade(result.getInt("pass_grade"));
                    reimb.setGrade_scheme(result.getInt("grade_scheme"));
                    reimb.setStart_date(result.getObject("start_date", LocalDate.class));
                    reimb.setSubmit_date(result.getObject("submit_date", LocalDate.class));
                    reimb.setSign_date(result.getObject("sign_date", LocalDate.class));
                    reimb.setLevel1(result.getInt("level1"));
                    reimb.setLevel2(result.getInt("level2"));
                    reimb.setLevel3(result.getInt("level3"));
                    reimb.setNext_signer(result.getInt("next_signer"));
                    reimb.setStatus(result.getInt("status"));
                    reimb.setNote(result.getString("note"));
                    reimb.setLocation(result.getString("location"));
                    reimb.setScore(result.getInt("score"));
                    reimb.setExceed(result.getBoolean("exceed"));
                    reimb.setNext_info(result.getInt("next_info"));
                    reimburses.add(reimb);
                }
            }
            return reimburses;
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public List<Reimburse> getAllReimburse(){
        //try with resources initialize resource that will be closed after we complete
        try(Connection con = cu.getConnection()){
            List<Reimburse> reimburses = new ArrayList<Reimburse>();
            String sql = "select * from \"TRMS1.3\".reimbursements";

            //avoid sql injection attacks
            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet result = ps.executeQuery();

            while(result.next()){
                Reimburse reimb = new Reimburse();
                reimb.setId(result.getInt("id"));
                reimb.setType(result.getInt("type"));
                reimb.setAmount(result.getFloat("amount"));
                reimb.setApplicant(result.getInt("applicant"));
                reimb.setPass_grade(result.getInt("pass_grade"));
                reimb.setGrade_scheme(result.getInt("grade_scheme"));
                reimb.setStart_date(result.getObject("start_date",LocalDate.class));
                reimb.setSubmit_date(result.getObject("submit_date",LocalDate.class));
                reimb.setSign_date(result.getObject("sign_date", LocalDate.class));
                reimb.setLevel1(result.getInt("level1"));
                reimb.setLevel2(result.getInt("level2"));
                reimb.setLevel3(result.getInt("level3"));
                reimb.setNext_signer(result.getInt("next_signer"));
                reimb.setStatus(result.getInt("status"));
                reimb.setNote(result.getString("note"));
                reimb.setLocation(result.getString("location"));
                reimb.setScore(result.getInt("score"));
                reimb.setExceed(result.getBoolean("exceed"));
                reimb.setNext_info(result.getInt("next_info"));
                reimburses.add(reimb);
            }
            return reimburses;
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public List<Reimburse> getAllOpenReimburse(){
        //try with resources initialize resource that will be closed after we complete
        try(Connection con = cu.getConnection()){
            List<Reimburse> reimburses = new ArrayList<Reimburse>();
            String sql = "select * from \"TRMS1.3\".reimbursements where status!=4 and status!=5 and status!=7";
            //avoid sql injection attacks
            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet result = ps.executeQuery();

            while(result.next()){
                Reimburse reimb = new Reimburse();
                reimb.setId(result.getInt("id"));
                reimb.setType(result.getInt("type"));
                reimb.setAmount(result.getFloat("amount"));
                reimb.setApplicant(result.getInt("applicant"));
                reimb.setPass_grade(result.getInt("pass_grade"));
                reimb.setGrade_scheme(result.getInt("grade_scheme"));
                reimb.setStart_date(result.getObject("start_date",LocalDate.class));
                reimb.setSubmit_date(result.getObject("submit_date",LocalDate.class));
                reimb.setSign_date(result.getObject("sign_date", LocalDate.class));
                reimb.setLevel1(result.getInt("level1"));
                reimb.setLevel2(result.getInt("level2"));
                reimb.setLevel3(result.getInt("level3"));
                reimb.setNext_signer(result.getInt("next_signer"));
                reimb.setStatus(result.getInt("status"));
                reimb.setNote(result.getString("note"));
                reimb.setLocation(result.getString("location"));
                reimb.setScore(result.getInt("score"));
                reimb.setExceed(result.getBoolean("exceed"));
                reimb.setNext_info(result.getInt("next_info"));
                reimburses.add(reimb);
            }
            return reimburses;
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    //Create, not allowed in app

    public Reimburse addReimburse(Reimburse reimb){
        try(Connection con=cu.getConnection()){
            String sql = "insert into \"TRMS1.3\".reimbursements " +
                    "(id,type,applicant,amount,start_date,submit_date,pass_grade,next_signer,grade_scheme,note,location,status)" +
                    " values " +
                    "(default,?,?,?,?,?,?,?,?,?,?,?) returning *";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1,reimb.getType());
            ps.setInt(2,reimb.getApplicant());
            ps.setFloat(3,reimb.getAmount());
            ps.setObject(4,reimb.getStart_date());
            ps.setObject(5,reimb.getSubmit_date());
            ps.setInt(6,reimb.getPass_grade());
            ps.setInt(7,reimb.getNext_signer());
            ps.setInt(8,reimb.getGrade_scheme());
            ps.setString(9,reimb.getNote());
            ps.setString(10,reimb.getLocation());
            ps.setInt(11,reimb.getStatus());

            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                Reimburse reimburse = new Reimburse();
                reimburse.setId(rs.getInt("id"));
                reimburse.setType(rs.getInt("type"));
                reimburse.setApplicant(rs.getInt("applicant"));
                reimburse.setAmount(rs.getFloat("amount"));
                reimburse.setStart_date(rs.getObject("start_date",LocalDate.class));
                reimburse.setSubmit_date(rs.getObject("submit_date",LocalDate.class));
                reimburse.setPass_grade(rs.getInt("pass_grade"));
                reimburse.setGrade_scheme(rs.getInt("grade_scheme"));
                reimburse.setLevel1(rs.getInt("level1"));
                reimburse.setLevel2(rs.getInt("level2"));
                reimburse.setLevel3(rs.getInt("level3"));
                reimburse.setNext_signer(rs.getInt("next_signer"));
                reimburse.setSign_date(rs.getObject("sign_date",LocalDate.class));
                reimburse.setStatus(rs.getInt("status"));
                reimburse.setNote(rs.getString("note"));
                reimburse.setLocation(rs.getString("location"));
                reimb.setScore(rs.getInt("score"));
                reimb.setExceed(rs.getBoolean("exceed"));
                reimb.setNext_info(rs.getInt("next_info"));
                return reimburse;
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    //Update
    public boolean updateReimburseAmount(Reimburse reimb){
        try(Connection con=cu.getConnection()){
            String sql = "update \"TRMS1.3\".reimbursements set amount=?, exceed=? where id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setFloat(1,reimb.getAmount());
            ps.setBoolean(2,reimb.getExceed());
            ps.setInt(3,reimb.getId());

            ps.executeUpdate();

            return true;
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateReimburseGradeScheme(Reimburse reimb){
        try(Connection con=cu.getConnection()){
            String sql = "update \"TRMS1.3\".reimbursements set grade_scheme=? where id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1,reimb.getGrade_scheme());
            ps.setInt(2,reimb.getId());

            ps.executeUpdate();

            return true;
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateReimburseSigner(Reimburse reimb){
        try(Connection con=cu.getConnection()){
            String sql = "update \"TRMS1.3\".reimbursements set next_signer=?, level1 =?, level2=?, level3=?, sign_date=? where id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1,reimb.getNext_signer());
            ps.setInt(2,reimb.getLevel1());
            ps.setInt(3,reimb.getLevel2());
            ps.setInt(4,reimb.getLevel3());
            ps.setObject(5,reimb.getSign_date());
            ps.setInt(6,reimb.getId());
            ps.executeUpdate();

            return true;
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateReimburseStatus(Reimburse reimb){
        try(Connection con=cu.getConnection()){
            String sql = "update \"TRMS1.3\".reimbursements set status=?, next_info=? where id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1,reimb.getStatus());
            ps.setInt(2,reimb.getNext_info());
            ps.setInt(3,reimb.getId());

            ps.executeUpdate();

            return true;
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateReimburseNote(Reimburse reimb){
        try(Connection con=cu.getConnection()){
            String sql = "update \"TRMS1.3\".reimbursements set note=? where id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1,reimb.getNote());
            ps.setInt(2,reimb.getId());

            ps.executeUpdate();

            return true;
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateReimburseScore(Reimburse reimb){
        try(Connection con=cu.getConnection()){
            String sql = "update \"TRMS1.3\".reimbursements set score=? where id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1,reimb.getScore());
            ps.setInt(2,reimb.getId());

            ps.executeUpdate();

            return true;
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    //Delete, not allowed in app
}



