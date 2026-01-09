package org.farm2.auth.service;

import org.farm2.auth.domain.AuthGrade;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;

import java.util.List;

/**职级 
 * @author cbtg自动生成  2026-1-5 11:41:21 
 */
public interface AuthGradeServiceInter {

    public AuthGrade insertAuthGradeEntity(AuthGrade authGrade);

    public AuthGrade editAuthGradeEntity(AuthGrade authGrade);

    public void delAuthGrade(String id);

    public AuthGrade getAuthGradeById(String id);

    public List<AuthGrade> getAuthGrades(DataQuery query);

    public DataResult searchAuthGrade(DataQuery query);

    public int getAuthGradeNum(DataQuery query);
    
    public int getNum(DataQuery query);
    /*[tree：树形结构使用]
    public void moveTreeNode(List<String> ids, String id);
    
    void autoSort(List<String> ids);
    */
}
