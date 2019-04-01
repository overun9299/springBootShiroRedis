package overun.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import overun.model.PermissionInit;
import overun.model.PermissionInitExample;

import java.util.List;

@Mapper
public interface PermissionInitMapper {
    long countByExample(PermissionInitExample example);

    int deleteByExample(PermissionInitExample example);

    int deleteByPrimaryKey(String id);

    int insert(PermissionInit record);

    int insertSelective(PermissionInit record);

    List<PermissionInit> selectByExample(PermissionInitExample example);

    PermissionInit selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") PermissionInit record, @Param("example") PermissionInitExample example);

    int updateByExample(@Param("record") PermissionInit record, @Param("example") PermissionInitExample example);

    int updateByPrimaryKeySelective(PermissionInit record);

    int updateByPrimaryKey(PermissionInit record);
}