package com.helger.phoss.smp.domain.pmigration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.id.IHasID;
import com.helger.commons.lang.EnumHelper;

/**
 * Defines the directions for SMP participant migrations.
 *
 * @author Philip Helger
 * @since 5.3.1
 */
public enum EParticipantMigrationDirection implements IHasID <String>
{
  /** Migrate a participant from this SMP to a different SMP. */
  OUTBOUND ("outbound"),
  /** Migrate a participant from another SMP to this SMP. */
  INBOUND ("inbound");

  private String m_sID;

  EParticipantMigrationDirection (@Nonnull @Nonempty final String sID)
  {
    m_sID = sID;
  }

  @Nonnull
  @Nonempty
  public String getID ()
  {
    return m_sID;
  }

  @Nullable
  public static EParticipantMigrationDirection getFromIDOrNull (@Nullable final String sID)
  {
    return EnumHelper.getFromIDOrNull (EParticipantMigrationDirection.class, sID);
  }
}
