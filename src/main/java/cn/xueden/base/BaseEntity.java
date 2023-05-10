package cn.xueden.base;

import cn.xueden.config.CustomAuditingListener;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Timestamp;

/**功能描述：公共Entity
 * @author:梁志杰
 * @date:2022/12/31
 * @description:cn.xueden.base
 * @version:1.0
 */
@Getter
@Setter
@MappedSuperclass
@EntityListeners(CustomAuditingListener.class)
public class BaseEntity implements Serializable {

    /**
     * 创建时间
     */
    @Column(name = "create_time",nullable = false)
    @CreationTimestamp
    private Timestamp createTime;

    /**
     * 创建者ID
     */
    @Column(name = "create_by",nullable = false)
    @CreatedBy
    private Long createBy;

    /**
     * 更新时间
     */
    @UpdateTimestamp
    @Column(name="update_time")
    private Timestamp updateTime;

    @Column(name="update_by",nullable = false)
    @LastModifiedBy
    private Long updateBy;

    /**
     * 备注
     */
    @Column(name = "remarks")
    private String remarks;

    public @interface Update {}

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        Field[] fields = this.getClass().getDeclaredFields();
        try {
            for (Field f : fields) {
                f.setAccessible(true);
                builder.append(f.getName(), f.get(this)).append("\n");
            }
        } catch (Exception e) {
            builder.append("toString builder encounter an error");
        }
        return builder.toString();
    }

}
